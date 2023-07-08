"use strict";

const React = require("react");
const follow = require("../utils/follow");
const client = require("axios");
const ReactDOM = require("react-dom");
var stompClient = require('../api/websocket-listener')

const root = "/api/orders"

class Orders extends React.Component {

    constructor(props) {
        super(props);
        this.state = {orders: [], attributes: [], page: 1, pageSize: 2, links: {}};
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onUpdate = this.onUpdate.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
        this.refreshAndGoToLastPage = this.refreshAndGoToLastPage.bind(this);
        this.refreshCurrentPage = this.refreshCurrentPage.bind(this);
    }

    // tag::follow-2[]
    loadFromServer(pageSize) {
        follow(client, root, [
            {rel: 'orders', params: {size: pageSize}}]
        ).then(ordersCollection => {
            return client({
                method: 'GET',
                url: ordersCollection.data._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
            }).then(schema => {
                this.schema = schema.data;
                this.links = ordersCollection.data._links;
                return ordersCollection;
            });
        }).then(ordersCollection => {
            return ordersCollection.data._embedded.orders.map(order =>
                client({
                    method: 'get',
                    url: order._links.self.href
                })
            );


        }).then(orderPromises => {
            let all = Promise.all(orderPromises);
            console.log(all);
            return all;
        }).then(orders => {
            console.log(orders);
            this.setState({
                orders: orders,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: this.links
            });
        });
    }
    // end::follow-2[]

    onUpdate(order, updatedOrder) {
        client({
            method: 'PUT',
            url: order.data._links.self.href,
            data: updatedOrder,
            headers: {
                'Content-Type': 'application/json',
                'If-Match': order.headers.etag
            }
        }).then(response => {
            //Web socket will handle that
        }, response => {
            if (response.response.status === 412) {
                alert('DENIED: Unable to update ' +
                    order.data._links.self.href + '. Your copy is stale.');
            }
        });
    }


    // tag::create[]
    onCreate(newOrder) {
        follow(client, root, ['orders']).then(ordersCollection => {
            client({
                method: 'POST',
                url: ordersCollection.data._links.self.href,
                data: newOrder,
                headers: {'Content-Type': 'application/json'}
            })
        });
    }
    // end::create[]

    // tag::delete[]
    onDelete(order) {
        client({method: 'DELETE', url: order.data._links.self.href});
    }
    // end::delete[]

    // tag::navigate[]
    onNavigate(navUri) {
        client({method: 'GET', url: navUri}).then(ordersCollection => {
            this.links = ordersCollection.data._links;

            return ordersCollection.data._embedded.orders.map(order =>
                client({
                    method: 'get',
                    url: order._links.self.href
                })
            );
        }).then(orderPromises => {
            return Promise.all(orderPromises);
        }).then(orders => {
            this.setState({
                orders: orders,
                attributes: Object.keys(this.schema.properties),
                pageSize: this.state.pageSize,
                links: this.links
            });
        })
    }
    // end::navigate[]

    // tag::update-page-size[]
    updatePageSize(pageSize) {
        if (pageSize !== this.state.pageSize) {
            this.loadFromServer(pageSize);
        }
    }
    // end::update-page-size[]

    refreshAndGoToLastPage(message) {
        follow(client, root, [{
            rel: 'orders',
            params: {size: this.state.pageSize}
        }]).then(response => {
            if (response.data._links.last !== undefined) {
                this.onNavigate(response.data._links.last.href);
            } else {
                this.onNavigate(response.data._links.self.href);
            }
        })
    }

    refreshCurrentPage(message) {
        follow(client, root, [{
            rel: 'orders',
            params: {
                size: this.state.pageSize,
                page: this.state.page.number
            }
        }]).then(ordersCollection => {
            this.links = ordersCollection.data._links;
            this.page = ordersCollection.data.page;

            return ordersCollection.data._embedded.orders.map(order => {
                return client({
                    method: 'GET',
                    url: order._links.self.href
                })
            });
        }).then(orderPromises => {
            return Promise.all(orderPromises);
        }).then(orders => {
            this.setState({
                page: this.page,
                orders: orders,
                attributes: Object.keys(this.schema.properties),
                pageSize: this.state.pageSize,
                links: this.links
            });
        });
    }

    // tag::follow-1[]
    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
        stompClient.register([
            {route: '/topic/newOrder', callback: this.refreshAndGoToLastPage},
            {route: '/topic/updateOrder', callback: this.refreshCurrentPage},
            {route: "/topic/deleteOrder", callback: this.refreshCurrentPage}
        ])
    }
    // end::follow-1[]

    render() {
        return (
            <div>
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>
                <OrderList orders={this.state.orders}
                           links={this.state.links}
                           pageSize={this.state.pageSize}
                           attributes={this.state.attributes}
                           onNavigate={this.onNavigate}
                           onUpdate={this.onUpdate}
                           onDelete={this.onDelete}
                           updatePageSize={this.updatePageSize}/>
            </div>
        )
    }
}

// tag::create-dialog[]
class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        const newOrder = {};
        this.props.attributes.forEach(attribute => {
            newOrder[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(newOrder);

        // clear out the dialog's inputs
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        const inputs = this.props.attributes.map(attribute =>
            <p key={attribute}>
                <input type="text" placeholder={attribute} ref={attribute} className="field"/>
            </p>
        );

        return (
            <div>
                <a href="#createOrder">Create</a>

                <div id="createOrder" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>

                        <h2>Create new order</h2>

                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }

}
// end::create-dialog[]

class OrderList extends React.Component {

    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    // tag::handle-page-size-updates[]
    handleInput(e) {
        e.preventDefault();
        const pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }
    // end::handle-page-size-updates[]

    // tag::handle-nav[]
    handleNavFirst(e){
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }
    // end::handle-nav[]

    // tag::employee-list-render[]
    render() {
        console.log(this);
        const orders = this.props.orders.map(order =>
            <Order key={order.data._links.self.href}
                   order={order}
                   attributes={this.props.attributes}
                   onDelete={this.props.onDelete}
                   onUpdate={this.props.onUpdate}
            />
        );

        const navLinks = [];
        if ("first" in this.props.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div>
                <input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput}/>
                <table>
                    <tbody>
                    <tr>
                        <th>Name</th>
                        <th>Date</th>
                        <th></th>
                        <th></th>
                    </tr>
                    {orders}
                    </tbody>
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        )
    }
    // end::employee-list-render[]
}

// tag::employee[]
class Order extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.order);
    }

    render() {
        return (
            <tr>
                <td>{this.props.order.data.name}</td>
                <td>{this.props.order.data.date}</td>
                <td>
                    <UpdateDialog order={this.props.order}
                                  attributes={this.props.attributes}
                                  onUpdate={this.props.onUpdate}/>
                </td>
                <td>
                    <button onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
        )
    }
}

class UpdateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        const updatedOrder = {};
        this.props.attributes.forEach(attribute => {
            updatedOrder[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onUpdate(this.props.order, updatedOrder);
        window.location = "#";
    }

    render() {
        const inputs = this.props.attributes.map(attribute =>
            <p key={this.props.order.data[attribute]}>
                <input type="text" placeholder={attribute}
                       defaultValue={this.props.order.data[attribute]}
                       ref={attribute} className="field"/>
            </p>
        );

        const dialogId = "updateEmployee-" + this.props.order.data._links.self.href;

        return (
            <div key={this.props.order.data._links.self.href}>
                <a href={"#" + dialogId}>Update</a>
                <div id={dialogId} className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>

                        <h2>Update an employee</h2>

                        <form>
                            {inputs}
                            <button onClick={this.handleSubmit}>Update</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }

}

export default Orders;
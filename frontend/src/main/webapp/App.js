'use strict';

// tag::vars[]
import NavBar from "./components/static/NavBar.jsx";
import Pages from "./components/Pages.jsx";
import styled from "styled-components";

const React = require('react'); // <1>
const ReactDOM = require('react-dom');
const client = require('./api/client'); // <3>
const follow = require('./utils/follow');

const root = '/api';

class App extends React.Component {
    render() {
        return (
            <>
                <Wrapper>
                    <NavBar />
                    <Pages />
                </Wrapper>
            </>
        );
    }
}

const Wrapper = styled.div`
  max-width: 100%;
  overflow: auto
`

export default App;

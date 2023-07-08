module.exports = function follow(api, rootPath, relArray) {
    const root = api({
        method: 'get',
        url: rootPath
    });

    // root.then(collection => console.log(collection));


    return relArray.reduce(function(root, arrayItem) {
        const rel = typeof arrayItem === 'string' ? arrayItem : arrayItem.rel;
        // console.log(traverseNext(root, rel, arrayItem));
        return traverseNext(root, rel, arrayItem);
    }, root);

    function traverseNext (root, rel, arrayItem) {
        return root.then(function (response) {
            if (hasEmbeddedRel(response.data, rel)) {
                return response.data._embedded[rel];
            }

            if(!response.data._links) {
                return [];
            }

            if (typeof arrayItem === 'string') {
                return api({
                    method: 'GET',
                    url: response.data._links[rel].href
                });
            } else {
                return api({
                    method: 'GET',
                    url: response.data._links[rel].href,
                    params: arrayItem.params
                });
            }
        });
    }

    function hasEmbeddedRel (data, rel) {
        return data._embedded && data._embedded.hasOwnProperty(rel);
    }
};
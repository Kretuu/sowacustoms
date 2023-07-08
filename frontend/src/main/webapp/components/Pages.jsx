import React from 'react';
import { Routes, Route } from "react-router-dom";
import Error404 from "../pages/Error404";
import Home from "../pages/Home";
import Orders from "./Orders";

function Pages() {
    return (
        <Routes>
            <Route path="/">
                <Route index element={ <Home /> } />
                <Route path="oferta" element={ <Orders /> } />
                <Route path="*" element={ <Error404 /> } />
            </Route>
        </Routes>
    );
}

export default Pages;

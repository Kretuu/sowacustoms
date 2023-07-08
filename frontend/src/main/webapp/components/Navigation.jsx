import React from 'react';
import styled from "styled-components";
import {Link} from "react-router-dom";

function Navigation({isOpened, switchSideMenu}) {
    return (
        <Nav className={isOpened ? 'active' : ''}>
            <OffCanvasHeader>Logo</OffCanvasHeader>
            <NavUl>
                <Item>
                    <NavLink to="/oferta" onClick={event => switchSideMenu()}>
                        <span>Oferta</span>
                    </NavLink>
                </Item>
                <Item>
                    <NavLink to="/axios" onClick={event => switchSideMenu()}>
                        <span>Realizacje</span>
                    </NavLink>
                </Item>
                <Item>
                    <NavLink to="/about" onClick={event => switchSideMenu()}>
                        <span>O nas</span>
                    </NavLink>
                </Item>
                <Item>
                    <NavLink to="/contact" onClick={event => switchSideMenu()}>
                        <span>Kontakt</span>
                    </NavLink>
                </Item>
            </NavUl>
        </Nav>
    );
}

const Nav = styled.nav.attrs(props => ({
    className: props.className,
}))`
  position: absolute;
  right: 145px;
  text-align: right;
  height: 80%;
  
  @media (max-width: 800px) {
    position: fixed;
    background-color: #ffffff;
    width: 50vw;
    right: -100%;
    top: 0;
    height: 100vh;
    z-index: 3;
    transition: all 0.5s ease;

    &.active {
      right: 0;
      transition: all 0.2s ease;
    }
  }
  
  
`

const NavUl = styled.ul`
  list-style: none;
  display: flex;
  flex-direction: row;
  padding: 0;
  margin: 0;
  height: 100%;
  
  @media (max-width: 800px) {
    flex-direction: column;
    height: auto;
  }
`

const Item = styled.li`
  display: block;
  width: 100px;
  height: 60%;
  margin: auto 0;
  font-size: 16px;
  color: #ffffff;
  &:hover {
    background-color: blue;
    transition: all 0.5s ease;
  }
  
  @media (max-width: 800px) {
    width: auto;
  }
`

const OffCanvasHeader = styled.div`
  display: none;
  text-align: center;
  padding: 15px;
  font-size: 1.5em;
  
  @media (max-width: 800px) {
    display: block;
  }
`

const NavLink = styled(Link)`
  color: #ffffff;
  text-decoration: none;
`


export default Navigation;

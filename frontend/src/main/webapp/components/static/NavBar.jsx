import React, {useEffect, useState} from 'react';
import './NavBar.css';
import Navigation from "../Navigation";
import styled from "styled-components";
import { SlMenu, SlClose } from 'react-icons/sl';
import {Link} from "react-router-dom";



function NavBar() {

    const [menuOpened, setMenuOpened] = useState(false);

    const switchSideMenu = () => {
        setMenuOpened(!menuOpened);
    };


    return (
        <header id="page-header">
            <div id="logo"><Link to="/"><h1>Logo</h1></Link></div>
            <Navigation isOpened={menuOpened} switchSideMenu={switchSideMenu} />
            <div id="make-booking">Zarezerwuj wizytę</div>
            <MenuButton className={menuOpened ? 'active' : ''} onClick={switchSideMenu}>{menuOpened ? <SlClose size={20} /> : <SlMenu size={20} />}</MenuButton>
            <Overlay className={menuOpened ? 'active' : ''}></Overlay>
        </header>
    );
}

const Overlay = styled.div.attrs(props => ({
    className: props.className,
}))`
  bottom: 0;
  right: 0;
  top: 0;
  left: 0;
  position: fixed;
  z-index: 2;
  backdrop-filter: blur(10px);
  visibility: hidden;

  @media (max-width: 800px) {
    transition: all 0.5s ease;
    opacity: 0;
    
    &.active {
      visibility: visible;
      opacity: 1;
    }
  }
`

const MenuButton = styled.div`
  position: absolute;
  right: 0;
  padding: 15px;
  margin: auto;
  z-index: 4;
  display: none;
  
  @media (max-width: 800px) {
    display: block;
    
    &.active {
      position: fixed;
    }
  }
`


export default NavBar;

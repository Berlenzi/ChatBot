import React from 'react';
import { ReactComponent as BotSvg } from '../assets/bot.svg';

function Header({ isOperator ,  isSidebarOpen }) {
  const name = isOperator ? 'Operator' : 'StageBot';
  return (
    <div className={`transition-all duration-300 ${isSidebarOpen ? 'ml-64' : 'ml-20'} mb-10`}>
      <div className= 'fixed w-full px-4 py-4 flex items-center space-x-10 md:bg-background '>
        <BotSvg />
        <p className={`text-2xl font-bold  text-center text-primary font-poppins ${isSidebarOpen ? 'hidden  md:flex' : ''}`}>{name}</p>
        
      </div>
    </div>
  );
}

export default Header;

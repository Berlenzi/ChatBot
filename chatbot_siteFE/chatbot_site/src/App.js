import logo from './logo.svg';
import { BrowserRouter as Router, Routes, Route , Navigate } from 'react-router-dom';
import LoginPage from './pages/login';
import RegisterPage from './pages/register';
import HomePage from './pages/Home.js';
import Users from './pages/users';
import PrivateRoute from './Components/PrivateRoute';

function App() {
  return (
    <div className=''>
      <Router>
        <Routes>
          {/*Public Route */}
          
          <Route path="/" element = {<LoginPage/>}/>
          <Route path="/register" element = {<RegisterPage/>}/>

           <Route
            path="/chat"
            element={
              <PrivateRoute>
                <HomePage />
              </PrivateRoute>
            }
          /> 
           <Route
            path="/users"
            element={
              <PrivateRoute>
                <Users />
              </PrivateRoute>
            }
          /> 
  
         
       

           

          
        </Routes>

      </Router>
    </div>
     

   

  );
}

export default App;

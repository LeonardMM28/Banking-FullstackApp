import './App.css';
import Tresor from './Tresor';
import Login from './Login';
import Create_Account from './Create_Account';
import Transactions from './Transactions';
import Contacts from './Contacts';

import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from './HomePage';

// https://reactrouter.com/en/main/start/examples
function App() {
  return (
    <BrowserRouter>
      <div>
        <Routes>
          <Route path="/Homepage" element={<HomePage />} />
          <Route path="/" element={<Tresor />} />
          <Route path="/Create_Account" element={<Create_Account />} />
          <Route path='/Transactions' element={<Transactions />} />
          <Route path='/Contacts' element={<Contacts />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;

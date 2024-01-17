/*
import "./contactsList.css";

// ContactList.js

import React, { useState } from 'react';

const ContactLists = () => {
  const [contacts, setContacts] = useState([]);
  const [name, setName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phone, setPhone] = useState('');

  const addContact = () => {
    // Create a new contact object
    const newContact = {
      name: name,
      lastName: lastName,
      phone: phone
    };

    // Update the contacts state
    setContacts([...contacts, newContact]);

    // Clear the form fields
    clearForm();
  };

  const clearForm = () => {
    setName('');
    setLastName('');
    setPhone('');
  };
  //add home redirect butto
  if(shouldRedirect){
    return <Navigate to="/home" replace={true} />;
}

  return (
    <div>
      <header>
        <h1>Contacts Page</h1>
      </header>
      <section id="contactsList">
        <h2>Contact List</h2>
        <ul>
          {contacts.map((contact, index) => (
            <li key={index}>
              <strong>{contact.name} {contact.lastName}</strong>
              <br />
              Phone: {contact.phone}
            </li>
          ))}
        </ul>
      </section>

      <section id="contactForm">
        <h2>Add a new contact</h2>
        <form>
          <label htmlFor="name">Name:</label>
          <input type="text" id="name" name="name" value={name} onChange={(e) => setName(e.target.value)} required />

          <label htmlFor="Lname">Last Name:</label>
          <input type="text" id="Lname" name="Lname" value={lastName} onChange={(e) => setLastName(e.target.value)} required />
          <br />

          <label htmlFor="phone">Phone:</label>
          <input type="tel" id="phone" name="phone" value={phone} onChange={(e) => setPhone(e.target.value)} required pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}" placeholder="Format: 123-456-7890" />
          <br />

          <button type="button" onClick={addContact}>Add Contact</button>
        </form>
      </section>
    </div>
  );
};

export default ContactLists;
/*
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import React from "react";
import "./contactsList.css";
import { Navigate } from "react-router-dom";
import { faHome } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";


export default function contactsList() {
  const [userName, setUserName] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [shouldRedirect, setShouldRedirect] = React.useState(false);
  const [message, setMessage] = React.useState("");

      function updateUserName(event) {
        setUserName(event.target.value); // updating react state variable
      }

      function updatePassword(event) {
        setPassword(event.target.value);
      }

      function register() {
        setMessage("");
        console.log("Registering " + userName + " " + password);
        // send request to back end
        const userDto = {
          userName: userName,
          password: password,
        };
        console.log(userDto);
        const options = {
          method: "POST",
          body: JSON.stringify(userDto),
        };
        fetch("/createUser", options) // network call = lag
          .then((res) => res.json()) // it worked, parse result6
          .then((apiRes) => {
            console.log(apiRes); // RestApiAppResponse
            if (apiRes.status) {
              // at the app layer, tell if worked or not
              setUserName("");
              setPassword("");
              setMessage("Your account has been created!");
              setShouldRedirect(true);
            } else {
              setMessage(apiRes.message); // tell end user why?
            }
          })
          .catch((error) => {
            console.log(error);
          }); // it did not work
      }
  
  if (shouldRedirect) {
    return <Navigate to="/HomePage" replace={true} />;
  }
  
  return (
    <div className="Create_Account">
      <header className="Create_Account-header">
        <img src="logo2.png" className="Create_Account-logo" alt="logo" />{" "}
        <h1 id="welcomeMessage">Create Account</h1>
        <div className="homeLogo">
          <FontAwesomeIcon
            icon={faHome}
            onClick={() => setShouldRedirect(true)}
          />
        </div>
      </header>
      <main>
        <div className="data">
          <div className="dataBox">
            <h2>Enter your information below:</h2>
            <div className="inputs">
              <TextField
                id="standard-basic"
                label="Username"
                variant="standard"
                value={userName}
                onChange={updateUserName}
                InputLabelProps={{ style: { fontSize: "1.2rem" } }}
              />
            </div>
            <div className="inputs">
              <TextField
                label="Password"
                id="standard-password-input"
                type="password"
                autoComplete="current-password"
                variant="standard"
                value={password}
                onChange={updatePassword}
                InputLabelProps={{ style: { fontSize: "1.2rem" } }}
              />
            </div>
            <div className="inputs">
              <TextField
                id="standard-password-input"
                label="Confirm Password"
                type="password"
                autoComplete="current-password"
                variant="standard"
                InputLabelProps={{ style: { fontSize: "1.2rem" } }}
              />
            </div>
            <Button variant="text" className="Submit" onClick={register}>
              Submit
            </Button>
          </div>
        </div>
      </main>
    </div>
  );
}
*/

import { faHome } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { Navigate } from "react-router-dom";

import "./Transactions.css";

export default function ContactsList() {
  const [amount, setAmount] = React.useState("");
  const [toId, setToId] = React.useState("");
  const [transactions, setTransactions] = React.useState([]);
  const [shouldRedirect, setShouldRedirect] = React.useState(false);

  const [userInfo, setUserInfo] = React.useState({
    userName: "",
    balance: 0,
  });
  const [balanceUpdated, setBalanceUpdated] = React.useState(false);
  const [selectedAction, setSelectedAction] = React.useState(null);
  const [countUpKey, setCountUpKey] = React.useState(0);
  const [typedWelcomeMessage, setTypedWelcomeMessage] = React.useState("");
  const [welcomeMessageIndex, setWelcomeMessageIndex] = React.useState(0);

  React.useEffect(() => {
    // triggers when componenet mounds
    // https://react.dev/reference/react/useEffect
    // fetching data
    // https://developer.mozilla.org/en-US/docs/Web/API/fetch
    fetchTransaction();
  }, []);
  
  if (shouldRedirect) {
    return <Navigate to="/HomePage" replace={true} />;
  }
  
  function fetchTransaction() {
    fetch("/getTransactions")
    .then((res) => res.json())
    .then((apiRes) => {
      console.log(apiRes);
      setTransactions(apiRes.data); // Ensure apiRes.data contains an array of transactions with uniqueId
    })
    .catch((error) => {
      console.error('Error fetching transactions:', error);
    });
}

  if (shouldRedirect) {
    return <Navigate to="/HomePage" replace={true} />;
  }

  function TruncatedText({ text, length = 10 }) {
    const [isTruncated, setIsTruncated] = React.useState(true);
  
    const toggleTruncated = () => {
      setIsTruncated(!isTruncated);
    };
  
    return (
      <span onClick={toggleTruncated} style={{ cursor: 'pointer' }}>
        {isTruncated ? `${text.substring(0, length)}...` : text}
      </span>
    );
  }

  return (
    <div className="Transactions">
      <header className="Transactions-header">
        <img src="logo2.png" className="Transactions-logo" alt="logo" />{" "}
        <h1 id="welcomeMessage">Recent Transactions</h1>
        <div className="homeLogo">
          <FontAwesomeIcon icon={faHome} onClick={() => setShouldRedirect(true)} className="header-home"/>
          <span className="home-text">Home</span>
        </div>
      </header>
      <main>
    <div className="content">
      <table>
        <thead>
          <tr>
            <th>Type</th>
            <th>Amount</th>
            <th>ID</th>
            <th>From user</th>
            <th>To user</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.uniqueId}>
              <td>{transaction.transactionType}</td>
              <td>${transaction.amount.toLocaleString()}</td>
              <td><TruncatedText text={transaction.uniqueId} length={10} /></td>
              <td>{transaction.userId}</td>
              <td>{transaction.toId}</td>
              <td>{new Date(transaction.timestamp).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </main>

    </div>
  );
}

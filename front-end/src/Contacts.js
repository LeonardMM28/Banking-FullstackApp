
import { faHome } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { Navigate } from "react-router-dom";

import "./ContactsList.css";

export default function Contacts() {
  const [shouldRedirect, setShouldRedirect] = React.useState(false);
  
  if (shouldRedirect) {
    return <Navigate to="/HomePage" replace={true} />;
  }
  
  // add a new contact
function addContact() {
  
  const name = document.getElementById('name').value;
  const lastName = document.getElementById('Lname').value;
  const phone = document.getElementById('phone').value;
  const account = document.getElementById('account').value;

  // Create a new contact 
  const contact = {
    name: name,
    lastName: lastName,
    phone: phone,
    account: account
  };

  // Adds the contact to the list
  addContactToList(contact);

 
  clearForm();
}

// Function to add a contact to the list
function addContactToList(contact) {
 
  const contactsListUl = document.getElementById('contactsListUl');

  // Create a new list item
  const listItem = document.createElement('li');

  
  listItem.innerHTML = `
    <strong>${contact.name} ${contact.lastName}</strong>
    <br>
    Phone: ${contact.phone}
    <br>
   Account: ${contact.account}
  `;

  
  listItem.classList.add('contact-item');

  
  contactsListUl.appendChild(listItem);
}


function clearForm() {
  document.getElementById('name').value = '';
  document.getElementById('Lname').value = '';
  document.getElementById('phone').value = '';
  document.getElementById('account').value = '';
}
  
  return (
    <div className="contacts">
      <header className="contacts-header">
        <img src="logo2.png" className="contacts-logo" alt="logo" />{" "}
        <h1 id="welcomeMessage">Contacts Page</h1>
        <div className="homeLogo">
          <FontAwesomeIcon
            icon={faHome}
            onClick={() => setShouldRedirect(true)}
            className="header-home"
          />
          <span className="home-text">Home</span>
        </div>
      </header>

      <h1>Contacts Page</h1>

      <section id="contactsList">
        <h2>Contacts List</h2>

        <ul id="contactsListUl"></ul>
      </section>

      <section id="contactForm">
        <h2>Add a new contact</h2>

        <form id="myForm">
          <label htmlFor="name">Name:</label>
          <input type="text" id="name" name="name" required />

          <label htmlFor="Lname">Last Name:</label>
          <input type="text" id="Lname" name="Lname" required />

           <label htmlFor="account">Account ID:</label>
          <input type="text" id="account" name="account" required />
          <br />

          <label htmlFor="phone">Phone:</label>
          <input
            type="tel"
            id="phone"
            name="phone"
            required
            pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}"
            placeholder="Format: 123-456-7890"
          />
          
          <br />

          <button type="button" onClick={addContact}>
            Add Contact
          </button>
        </form>
      </section>
    </div>
  );
}



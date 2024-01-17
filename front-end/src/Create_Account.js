import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import React from "react";
import "./Create_Account.css";
import { Navigate } from "react-router-dom";
import { faHome } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";


export default function Create_Account() {
  const [userName, setUserName] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [shouldRedirect, setShouldRedirect] = React.useState(false);
  const [message, setMessage] = React.useState("");

      function updateUserName(event) {
        setUserName(event.target.value); 
      }

      function updatePassword(event) {
        setPassword(event.target.value);
      }

      function register() {
        setMessage("");
        console.log("Registering " + userName + " " + password);
        const userDto = {
          userName: userName,
          password: password,
        };
        console.log(userDto);
        const options = {
          method: "POST",
          body: JSON.stringify(userDto),
        };
        fetch("/createUser", options) 
          .then((res) => res.json()) 
          .then((apiRes) => {
            console.log(apiRes);
            if (apiRes.status) {
              setUserName("");
              setPassword("");
              setMessage("Your account has been created!");
              setShouldRedirect(true);
            } else {
              setMessage(apiRes.message); 
            }
          })
          .catch((error) => {
            console.log(error);
          }); // it did not work
      }
  
  if (shouldRedirect) {
    return <Navigate to="/" replace={true} />;
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
          <span className="home-text">Home</span>
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

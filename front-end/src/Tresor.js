import { faArrowDown } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Navigate } from "react-router-dom";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import React from "react";
import { useNavigate } from "react-router-dom";

import "./Tresor.css";

export default function Tresor() {
  const [scrollPosition, setScrollPosition] = React.useState(0);
  const [firstLogoOpacity, setFirstLogoOpacity] = React.useState(1); 
  const [secondLogoOpacity, setSecondLogoOpacity] = React.useState(0);
  const [isAnimationTriggered, setIsAnimationTriggered] = React.useState(false);
  const [userName, setUserName] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [shouldRedirect, setShouldRedirect] = React.useState(false);
  const [message, setMessage] = React.useState("");
  const navigate = useNavigate();


  function handleScroll() {
    requestAnimationFrame(() => {
      const newScrollPosition = window.scrollY;

      const opacity1 = Math.min(1, newScrollPosition / 500);
      const opacity2 = Math.min(1, newScrollPosition / 400);

      setSecondLogoOpacity(opacity1);
      setFirstLogoOpacity(1 - opacity2);

      if (newScrollPosition > 200 && !isAnimationTriggered) {
        setIsAnimationTriggered(true);
      }

      setScrollPosition(newScrollPosition);
    });
  }

  React.useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [scrollPosition, isAnimationTriggered]);

  const minImageSize = 10; 
  const imageSize = Math.max(minImageSize, 50 - scrollPosition * 0.1);
  const coverImgStyle = {
    transform: `translateY(${scrollPosition}px)`,
  };

    function updateUserName(event) {
      setUserName(event.target.value); 
    }

    function updatePassword(event) {
      setPassword(event.target.value);
    }

    function logIn() {
      setMessage("");
      console.log("Loging in " + userName + " " + password);
      const userDto = {
        userName: userName,
        password: password,
      };
      console.log(userDto);
      const options = {
        method: "POST",
        body: JSON.stringify(userDto),
      };
      fetch("/login", options) // network call = lag
        //.then((res) => res.json()) // it worked, parse result
        .then((apiRes) => {
          console.log(apiRes);
          if (apiRes.ok) {
            console.log("Login worked");
            setShouldRedirect(true);
          } else {
            setMessage("Failed to log in");
          }
          console.log("Worked"); // RestApiAppResponse
        })
        .catch((error) => {
          console.log(error);
          setMessage("Failed to log in");
        }); // it did not work
    }

    if (shouldRedirect) {
      return <Navigate to="/Homepage" replace={true} />;
    }
  
  function redirectToCreateAccount() {
    navigate("/Create_Account");
  }

  return (
    <div className="Tresor">
      <header className="Tresor-header">
        <img
          src="logo2.png"
          className="Tresor-logo"
          alt="logo"
          style={{ opacity: secondLogoOpacity }}
        />{" "}
      </header>
      <main>
        <div
          className="coverImg"
          style={{ ...coverImgStyle, opacity: firstLogoOpacity }}
        >
          <img
            src="logo.png"
            alt="logo"
            style={{ width: `${imageSize}%`, zIndex: 1000 }}
          />
          <h1 className="scroll">Scroll down to get started</h1>
          <FontAwesomeIcon icon={faArrowDown} className="arrowD" />
        </div>
        <div className="data">
          <div className={`dataBox ${isAnimationTriggered ? "animate" : ""}`}>
            <h2>Please type your credentials:</h2>
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
                value={password}
                onChange={updatePassword}
                autoComplete="current-password"
                variant="standard"
                InputLabelProps={{ style: { fontSize: "1.2rem" } }}
              />
            </div>

            <Button variant="text" className="Submit" onClick={logIn}>
              Access Vault
            </Button>
            <Button variant="text" className="Submit2" onClick={redirectToCreateAccount}>
              Create Account
            </Button>
          </div>
        </div>
      </main>
    </div>
  );
}

import React from 'react';
import { Navigate } from 'react-router-dom';

export default function Login() {
    const [userName, setUserName] = React.useState('');
    const [password, setPassword] = React.useState('');
    const [shouldRedirect, setShouldRedirect] = React.useState(false);
    const [message, setMessage] = React.useState('');

    function updateUserName(event) {
        setUserName(event.target.value); 
    }

    function updatePassword(event) {
        setPassword(event.target.value);
    }

    function register() {
        setMessage('');
        console.log('Registering ' + userName + ' ' + password);
        const userDto = {
            userName: userName,
            password: password
        };
        console.log(userDto);
        const options = {
            method: 'POST',
            body: JSON.stringify(userDto)
        };
        fetch('/createUser', options) // network call = lag
            .then((res) => res.json()) // it worked, parse result6
            .then((apiRes) => {
                console.log(apiRes); // RestApiAppResponse
                if (apiRes.status) { // at the app layer, tell if worked or not
                    setUserName('');
                    setPassword('');
                    setMessage('Your account has been created!');
                } else {
                    setMessage(apiRes.message); // tell end user why?
                }
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }

    function logIn() {
        setMessage('');
        console.log('Loging in ' + userName + ' ' + password);
        const userDto = {
            userName: userName,
            password: password
        };
        console.log(userDto);
        const options = {
            method: 'POST',
            body: JSON.stringify(userDto)
        };
        fetch('/login', options) 
            .then((apiRes) => {
                console.log(apiRes);
                if(apiRes.ok){
                    console.log('Login worked');
                    setShouldRedirect(true);
                }else{
                    setMessage('Failed to log in');
                }
                console.log('Worked'); 

            })
            .catch((error) => {
                console.log(error);
                setMessage('Failed to log in');
            }) 
    }

    if(shouldRedirect){
        return <Navigate to="/home" replace={true} />;
    }

    return (
        <div>
            <h1>Log in into your account</h1>
            {message}
            <div>
                <input value={userName} onChange={updateUserName} />

                <input type="password" value={password} onChange={updatePassword} />
            </div>
            <div>
                <button onClick={register}>Register</button>
                <button onClick={logIn}>Login</button>
            </div>
        </div>
    );
}
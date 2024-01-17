import { faHome } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { Navigate } from "react-router-dom";

import "./Transactions.css";

export default function Transactions() {
  const [transactions, setTransactions] = React.useState([]);
  const [shouldRedirect, setShouldRedirect] = React.useState(false);

  React.useEffect(() => {
    // triggers when componenet mounds
    // https://react.dev/reference/react/useEffect
    // fetching data
    // https://developer.mozilla.org/en-US/docs/Web/API/fetch
    fetchTransaction();
  }, []);

  function fetchTransaction() {
    fetch("/getTransactions")
      .then((res) => res.json())
      .then((apiRes) => {
        console.log(apiRes);
        setTransactions(apiRes.data);
      })
      .catch((error) => {
        console.error("Error fetching transactions:", error);
      });
  }

  if (shouldRedirect) {
    return <Navigate to="/HomePage" replace={true} />;
  }

  function TruncatedText({ text = " ", length = 10 }) {
    const [isTruncated, setIsTruncated] = React.useState(true);

    const toggleTruncated = () => {
      setIsTruncated(!isTruncated);
    };

    return (
      <span onClick={toggleTruncated} style={{ cursor: "pointer" }}>
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
          <FontAwesomeIcon
            icon={faHome}
            onClick={() => setShouldRedirect(true)}
            className="header-home"
          />
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
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((transaction) => (
                <tr key={transaction.uniqueId}>
                  <td>
                    {transaction.transactionType}: {transaction.cryptoType}{" "}
                    <TruncatedText text={transaction.toId} length={10} />
                  </td>
                  <td>${transaction.amount.toLocaleString()}</td>
                  <td>
                    <TruncatedText text={transaction.uniqueId} length={10} />
                  </td>
                  <td>{transaction.userId}</td>
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

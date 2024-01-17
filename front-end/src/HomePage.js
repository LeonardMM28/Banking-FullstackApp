import {
  faArrowRight,
  faArrowTrendDown,
  faArrowTrendUp,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Button from "@mui/material/Button";
import FilledInput from "@mui/material/FilledInput";
import FormControl from "@mui/material/FormControl";
import InputAdornment from "@mui/material/InputAdornment";
import InputLabel from "@mui/material/InputLabel";
import TextField from "@mui/material/TextField";
import React,{useState,useEffect} from "react";
import CountUp from "react-countup";
import { Navigate, useNavigate } from "react-router-dom";


import "./HomePage.css";

export default function HomePage() {
  const [amount, setAmount] = React.useState("");
  const [toId, setToId] = React.useState("");
  const [shouldRedirect, setShouldRedirect] = React.useState(false);
  const navigate = useNavigate(); // Initialize the useNavigate hook

  const [userInfo, setUserInfo] = React.useState({
    userName: "",
    balance: 0,
    BTC: 0,
    ETH: 0,
    LTC: 0,
    DOGE: 0,
    XLM: 0,
  });

  const [cryptoPrices, setCryptoPrices] = React.useState({
    BTC: 0,
    ETH: 0,
    LTC: 0,
    DOGE: 0,
    XLM: 0,
  });

  const [cryptoAmounts, setCryptoAmounts] = React.useState({
    BTC: 0,
    ETH: 0,
    LTC: 0,
    DOGE: 0,
    XLM: 0,
  });

  const [cryptoDynamicValues, setCryptoDynamicValues] = React.useState({
    BTC: 0,
    ETH: 0,
    LTC: 0,
    DOGE: 0,
    XLM: 0,
  });

  const [balanceUpdated, setBalanceUpdated] = React.useState(false);
  const [selectedAction, setSelectedAction] = React.useState(null);

  function fetchUserInfo() {
    fetch("/getUser")
      .then((res) => res.json())
      .then((apiRes) => {
        console.log(apiRes);
        setUserInfo(apiRes.data[0]);
        setBalanceUpdated(true);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function Logout() {
    fetch("/logout")
      .then((res) => res.json(navigate("/")))
      .then((apiRes) => {
        console.log(apiRes);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function handleActionClick(action) {
    setSelectedAction(action);
  }

  function updateAmount(event) {
    const numberValue = Number(event.target.value);
    console.log(numberValue);
    setAmount(event.target.value);
  }

  function updateToId(event) {
    const stringValue = String(event.target.value);
    console.log(stringValue);
    setToId(event.target.value);
  }

  function deposit() {
    const transactionDto = {
      amount: Number(amount),
    };
    const options = {
      method: "POST",
      body: JSON.stringify(transactionDto),
      credentials: "include",
    };
    fetch("/createDeposit", options)
      .then((res) => res.json())
      .then((apiRes) => {
        console.log(apiRes);
        setAmount("");
        window.location.reload();
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function transfer() {
    const transactionDto = {
      amount: Number(amount),
      toId: String(toId),
    };
    const options = {
      method: "POST",
      body: JSON.stringify(transactionDto),
      credentials: "include",
    };
    fetch("/transfer", options)
      .then((res) => res.json())
      .then((apiRes) => {
        console.log(apiRes);
        setAmount("");
        setToId("");
        window.location.reload();
      })
      .catch((error) => {
        console.log(error);
      });
  }

  async function withdraw() {
    const transactionDto = {
      amount: Number(amount),
    };
    const options = {
      method: "POST",
      body: JSON.stringify(transactionDto),
      credentials: "include",
    };
    const result = await fetch("/withdraw", options);
    const apiRes = await result.json();
    setAmount("");
    window.location.reload();
  }

  function generateRandomPrice(min, max) {
    const randomNumber = Math.random() * (max - min) + min;
    const roundedNumber = Number(randomNumber.toFixed(5));
    return roundedNumber;
  }

  function calculateCryptoDynamicValue(crypto, amount, price) {
    return amount * price;
  }

  function updateCryptoAmount(crypto, value) {
    const newCryptoAmounts = { ...cryptoAmounts, [crypto]: value };
    setCryptoAmounts(newCryptoAmounts);

    const currentPrice = cryptoPrices[crypto];

    const dynamicValue = calculateCryptoDynamicValue(
      crypto,
      value,
      currentPrice
    );
    setCryptoDynamicValues({ ...cryptoDynamicValues, [crypto]: dynamicValue });
  }

  function buyBTC() {
    if (
      window.confirm(`Are you sure you want to buy ${cryptoAmounts.BTC} BTC?`)
    ) {
      const requestData = {
        btcAmount: Number(cryptoAmounts.BTC),
        btcPrice: Number(cryptoPrices.BTC),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/buyBTC", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, BTC: "" });
          setCryptoPrices({ ...cryptoPrices, BTC: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function sellBTC() {
    if (
      window.confirm(`Are you sure you want to sell ${cryptoAmounts.BTC} BTC?`)
    ) {
      const requestData = {
        btcAmount: Number(cryptoAmounts.BTC),
        btcPrice: Number(cryptoPrices.BTC),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/sellBTC", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, BTC: "" });
          setCryptoPrices({ ...cryptoPrices, BTC: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function buyETH() {
    if (
      window.confirm(`Are you sure you want to buy ${cryptoAmounts.ETH} ETH?`)
    ) {
      const requestData = {
        ethAmount: Number(cryptoAmounts.ETH),
        ethPrice: Number(cryptoPrices.ETH),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/buyETH", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, ETH: "" });
          setCryptoPrices({ ...cryptoPrices, ETH: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function sellETH() {
    if (
      window.confirm(`Are you sure you want to sell ${cryptoAmounts.ETH} ETH?`)
    ) {
      const requestData = {
        ethAmount: Number(cryptoAmounts.ETH),
        ethPrice: Number(cryptoPrices.ETH),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/sellETH", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, ETH: "" });
          setCryptoPrices({ ...cryptoPrices, ETH: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function buyLTC() {
    if (
      window.confirm(`Are you sure you want to buy ${cryptoAmounts.LTC} LTC?`)
    ) {
      const requestData = {
        ltcAmount: Number(cryptoAmounts.LTC),
        ltcPrice: Number(cryptoPrices.LTC),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/buyLTC", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, LTC: "" });
          setCryptoPrices({ ...cryptoPrices, LTC: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function sellLTC() {
    if (
      window.confirm(`Are you sure you want to sell ${cryptoAmounts.LTC} LTC?`)
    ) {
      const requestData = {
        ltcAmount: Number(cryptoAmounts.LTC),
        ltcPrice: Number(cryptoPrices.LTC),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/sellLTC", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, LTC: "" });
          setCryptoPrices({ ...cryptoPrices, LTC: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function buyDOGE() {
    if (
      window.confirm(`Are you sure you want to buy ${cryptoAmounts.DOGE} DOGE?`)
    ) {
      const requestData = {
        dogeAmount: Number(cryptoAmounts.DOGE),
        dogePrice: Number(cryptoPrices.DOGE),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/buyDOGE", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, DOGE: "" });
          setCryptoPrices({ ...cryptoPrices, DOGE: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function sellDOGE() {
    if (
      window.confirm(
        `Are you sure you want to sell ${cryptoAmounts.DOGE} DOGE?`
      )
    ) {
      const requestData = {
        dogeAmount: Number(cryptoAmounts.DOGE),
        dogePrice: Number(cryptoPrices.DOGE),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/sellDOGE", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, DOGE: "" });
          setCryptoPrices({ ...cryptoPrices, DOGE: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function buyXLM() {
    if (
      window.confirm(`Are you sure you want to buy ${cryptoAmounts.XLM} XLM?`)
    ) {
      const requestData = {
        xlmAmount: Number(cryptoAmounts.XLM),
        xlmPrice: Number(cryptoPrices.XLM),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/buyXLM", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, XLM: "" });
          setCryptoPrices({ ...cryptoPrices, XLM: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function sellXLM() {
    if (
      window.confirm(`Are you sure you want to sell ${cryptoAmounts.XLM} XLM?`)
    ) {
      const requestData = {
        xlmAmount: Number(cryptoAmounts.XLM),
        xlmPrice: Number(cryptoPrices.XLM),
      };

      const options = {
        method: "POST",
        body: JSON.stringify(requestData),
        credentials: "include",
      };

      fetch("/sellXLM", options)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setCryptoAmounts({ ...cryptoAmounts, XLM: "" });
          setCryptoPrices({ ...cryptoPrices, XLM: "" });
          window.location.reload();
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }

  function determineArrowDirection(crypto, averagePrice) {
    const currentPrice = cryptoPrices[crypto];
    return currentPrice > averagePrice ? "up" : "down";
  }

  function updateMonth() {
    const now = new Date();
    const monthElement = document.getElementById("monthDisplay");
    const options = { month: "long" };

    if (monthElement) {
      monthElement.textContent = now.toLocaleDateString("en-US", options);
    }
  }
  setInterval(updateMonth, 1000);

  const SpendingSummary = () => {
    const [spendingSummary, setSpendingSummary] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
  
    useEffect(() => {
      const userId = toId; 

      fetch(`/api/spendingSummary?userId=${userId}`)
        .then((res) => res.json())
        .then((apiRes) => {
          console.log(apiRes);
          setSpendingSummary(apiRes.data[0]);
          setLoading(false);
        })
        .catch((error) => {
          console.log(error);
          setLoading(false);
        });
    }, []);}
  
  React.useEffect(() => {
    setCryptoPrices({
      BTC: generateRandomPrice(50000, 70000),
      ETH: generateRandomPrice(3000, 5000),
      LTC: generateRandomPrice(150, 250),
      DOGE: generateRandomPrice(0.2, 0.5),
      XLM: generateRandomPrice(0.3, 0.7),
    });
    fetchUserInfo();
  }, []);

  if (shouldRedirect) {
    return <Navigate to="/Transactions" replace={true} />;
  }

  return (
    <div className="HomePage">
      <header className="HomePage-header">
        <img src="logo2.png" className="HomePage-logo" alt="logo" />
        <h1 id="welcomeMessage">Welcome to your vault {userInfo.userName}</h1>
        <div className="logout" onClick={Logout}>
          <FontAwesomeIcon icon={faArrowRight} className="header-arrow" />
          <span className="logout-text">Logout</span>
        </div>
      </header>
      <main>
        <div className="content">
          <div className="block1">
            <div className="info">
              <h1>Account Balance</h1>
              <h1
                id="number"
                className={balanceUpdated ? "balance-updated" : ""}
              >
                ${" "}
                <CountUp
                  start={100}
                  end={userInfo.balance.toFixed(3)}
                  duration={1.5}
                  decimals={3}
                />
              </h1>{" "}
              <h2 onClick={() => setShouldRedirect(true)}>View transactions</h2>
            </div>
            <div className="actions">
              <div className="dropdown-btn">
                <h1>Actions</h1>
                <div className="dropdown-content">
                  <a onClick={() => handleActionClick("Withdraw")}>Withdraw</a>
                  <a onClick={() => handleActionClick("Deposit")}>Deposit</a>
                  <a onClick={() => handleActionClick("Transfer")}>Transfer</a>
                  <a onClick={() => setShouldRedirect(true)}>Account History</a>
                  <a href="/Contacts">Contacts List</a>
                </div>
              </div>
            </div>
            <div className="actionInfo">
              <div
                className={`withdraw ${
                  selectedAction === "Withdraw" ? "visible" : ""
                }`}
              >
                <h1>Withdraw</h1>
                <div id="inner">
                  <FormControl sx={{ width: "13rem" }} variant="filled">
                    {" "}
                    <InputLabel htmlFor="filled-adornment-amount">
                      Amount
                    </InputLabel>
                    <FilledInput
                      id="filled-adornment-amount"
                      value={amount}
                      onChange={updateAmount}
                      startAdornment={
                        <InputAdornment position="start">$</InputAdornment>
                      }
                    />
                  </FormControl>

                  <Button id="button" variant="outlined" onClick={withdraw}>
                    Submit
                  </Button>
                </div>
              </div>
              <div
                className={`deposit ${
                  selectedAction === "Deposit" ? "visible" : ""
                }`}
              >
                <h1>Deposit</h1>
                <div id="inner">
                  <FormControl sx={{ width: "13rem" }} variant="filled">
                    {" "}
                    <InputLabel htmlFor="filled-adornment-amount">
                      Amount
                    </InputLabel>
                    <FilledInput
                      id="filled-adornment-amount"
                      value={amount}
                      onChange={updateAmount}
                      startAdornment={
                        <InputAdornment position="start">$</InputAdornment>
                      }
                    />
                  </FormControl>

                  <Button id="button" variant="outlined" onClick={deposit}>
                    Submit
                  </Button>
                </div>
              </div>
              <div
                className={`transfer ${
                  selectedAction === "Transfer" ? "visible" : ""
                }`}
              >
                <h1>Transfer</h1>
                <div id="inner">
                  <div id="transferInfo">
                    <FormControl sx={{ width: "13rem" }} variant="filled">
                      {" "}
                      <InputLabel htmlFor="filled-adornment-amount">
                        Amount
                      </InputLabel>
                      <FilledInput
                        id="filled-adornment-amount"
                        value={amount}
                        onChange={updateAmount}
                        startAdornment={
                          <InputAdornment position="start">$</InputAdornment>
                        }
                      />
                    </FormControl>
                    <FormControl sx={{ width: "13rem" }} variant="filled">
                      {" "}
                      <InputLabel
                        htmlFor="filled-adornment-amount"
                        value={toId}
                        onChange={updateToId}
                      >
                        Recipient Account ID
                      </InputLabel>
                      <FilledInput
                        id="filled-adornment-amount"
                        value={toId}
                        onChange={updateToId}
                        startAdornment={
                          <InputAdornment position="start">#</InputAdornment>
                        }
                      />
                    </FormControl>
                  </div>

                  <Button id="button" variant="outlined" onClick={transfer}>
                    Submit
                  </Button>
                </div>
              </div>
            </div>
          </div>
          <div className="block2">
            <h1 id="title">Crypto Wallet</h1>
            <div className="cryptos">
              <div className="crypto">
                <h1>BITCOIN</h1>
                <h1>{userInfo.BTC} BTC</h1>
                <div id="cryptoActions">
                  <TextField
                    id="filled-number"
                    label="Amount"
                    type="number"
                    InputLabelProps={{
                      shrink: true,
                    }}
                    variant="filled"
                    sx={{
                      width: "70%",
                    }}
                    onChange={(event) =>
                      updateCryptoAmount("BTC", event.target.value)
                    }
                  />
                  <div id="buySell">
                    <button id="buy" onClick={buyBTC}>
                      BUY
                    </button>
                    <button id="sell" onClick={sellBTC}>
                      SELL
                    </button>
                  </div>
                </div>
                <div id="price">
                  <h2>Current Price: $ {cryptoPrices.BTC}</h2>
                  {determineArrowDirection("BTC", 60000) === "up" ? (
                    <FontAwesomeIcon id="arrowup" icon={faArrowTrendUp} />
                  ) : (
                    <FontAwesomeIcon id="arrowdown" icon={faArrowTrendDown} />
                  )}
                </div>
                <h2>VALUE: $ {cryptoDynamicValues.BTC.toFixed(4)}</h2>
              </div>
              <div className="crypto">
                <h1>ETHERIUM</h1>
                <h1>{userInfo.ETH} ETH</h1>
                <div id="cryptoActions">
                  <TextField
                    id="filled-number"
                    label="Amount"
                    type="number"
                    InputLabelProps={{
                      shrink: true,
                    }}
                    variant="filled"
                    sx={{
                      width: "70%",
                    }}
                    onChange={(event) =>
                      updateCryptoAmount("ETH", event.target.value)
                    }
                  />
                  <div id="buySell">
                    <button id="buy" onClick={buyETH}>
                      BUY
                    </button>
                    <button id="sell" onClick={sellETH}>
                      SELL
                    </button>
                  </div>
                </div>
                <div id="price">
                  <h2>Current Price: $ {cryptoPrices.ETH}</h2>
                  {determineArrowDirection("ETH", 4000) === "up" ? (
                    <FontAwesomeIcon id="arrowup" icon={faArrowTrendUp} />
                  ) : (
                    <FontAwesomeIcon id="arrowdown" icon={faArrowTrendDown} />
                  )}
                </div>
                <h2>VALUE: $ {cryptoDynamicValues.ETH.toFixed(4)}</h2>
              </div>
              <div className="crypto">
                <h1>LITECOIN</h1>
                <h1>{userInfo.LTC} LTC</h1>
                <div id="cryptoActions">
                  <TextField
                    id="filled-number"
                    label="Amount"
                    type="number"
                    InputLabelProps={{
                      shrink: true,
                    }}
                    variant="filled"
                    sx={{
                      width: "70%",
                    }}
                    onChange={(event) =>
                      updateCryptoAmount("LTC", event.target.value)
                    }
                  />
                  <div id="buySell">
                    <button id="buy" onClick={buyLTC}>
                      BUY
                    </button>
                    <button id="sell" onClick={sellLTC}>
                      SELL
                    </button>
                  </div>
                </div>
                <div id="price">
                  <h2>Current Price: $ {cryptoPrices.LTC}</h2>
                  {determineArrowDirection("LTC", 200) === "up" ? (
                    <FontAwesomeIcon id="arrowup" icon={faArrowTrendUp} />
                  ) : (
                    <FontAwesomeIcon id="arrowdown" icon={faArrowTrendDown} />
                  )}
                </div>
                <h2>VALUE: $ {cryptoDynamicValues.LTC.toFixed(4)}</h2>
              </div>
              <div className="crypto">
                <h1>DOGECOIN</h1>
                <h1>{userInfo.DOGE} DOGE</h1>
                <div id="cryptoActions">
                  <TextField
                    id="filled-number"
                    label="Amount"
                    type="number"
                    InputLabelProps={{
                      shrink: true,
                    }}
                    variant="filled"
                    sx={{
                      width: "70%",
                    }}
                    onChange={(event) =>
                      updateCryptoAmount("DOGE", event.target.value)
                    }
                  />
                  <div id="buySell">
                    <button id="buy" onClick={buyDOGE}>
                      BUY
                    </button>
                    <button id="sell" onClick={sellDOGE}>
                      SELL
                    </button>
                  </div>
                </div>
                <div id="price">
                  <h2>Current Price: $ {cryptoPrices.DOGE}</h2>
                  {determineArrowDirection("DOGE", 0.35) === "up" ? (
                    <FontAwesomeIcon id="arrowup" icon={faArrowTrendUp} />
                  ) : (
                    <FontAwesomeIcon id="arrowdown" icon={faArrowTrendDown} />
                  )}
                </div>
                <h2>VALUE: $ {cryptoDynamicValues.DOGE.toFixed(4)}</h2>
              </div>
              <div className="crypto">
                <h1>STELLAR</h1>
                <h1>{userInfo.XLM} XLM</h1>
                <div id="cryptoActions">
                  <TextField
                    id="filled-number"
                    label="Amount"
                    type="number"
                    InputLabelProps={{
                      shrink: true,
                    }}
                    variant="filled"
                    sx={{
                      width: "70%",
                    }}
                    onChange={(event) =>
                      updateCryptoAmount("XLM", event.target.value)
                    }
                  />
                  <div id="buySell">
                    <button id="buy" onClick={buyXLM}>
                      BUY
                    </button>
                    <button id="sell" onClick={sellXLM}>
                      SELL
                    </button>
                  </div>
                </div>
                <div id="price">
                  <h2>Current Price: $ {cryptoPrices.XLM}</h2>
                  {determineArrowDirection("XLM", 0.5) === "up" ? (
                    <FontAwesomeIcon id="arrowup" icon={faArrowTrendUp} />
                  ) : (
                    <FontAwesomeIcon id="arrowdown" icon={faArrowTrendDown} />
                  )}
                </div>
                <h2>VALUE: $ {cryptoDynamicValues.XLM.toFixed(4)}</h2>
              </div>
            </div>
          </div>

          <div className="block3">
            <h1>
              <u>Spending Summary</u>
            </h1>
            <div id="monthContainer">
              <h1 id="sum">{SpendingSummary?.amount}</h1>
              <div className="monthSum">
                <h2>
                  <span id="monthDisplay"></span> Summary{" "}
                </h2>
              </div>

              <div className="chart-wrap vertical">
                <div className="grid">
                  <div className="grid-label">
                    <li>
                      <div>$800</div>
                    </li>
                    <li>
                      <div>$500</div>
                    </li>
                    <li>
                      <div>$300</div>
                    </li>
                  </div>
                  <div
                    className="bar"
                    style={{ marginRight: 5 + "em" }}
                    data-name="Aug"
                    title="August"
                  ></div>
                  <div
                    className="bar"
                    style={{ marginRight: 6 + "em" }}
                    data-name="Sept"
                    title="September"
                  ></div>
                  <div
                    className="bar"
                    style={{ marginRight: 3 + "em" }}
                    data-name="Oct"
                    title="October"
                  ></div>
                  <div
                    className="bar"
                    style={{ marginRight: 5 + "em" }}
                    data-name="Nov"
                    title="November"
                  ></div>
                  <div
                    className="bar"
                    style={{ marginRight: 19 + "em" }}
                    data-name="Dec"
                    title="December"
                  ></div>
                </div>
              </div>
              <section className="box">
                <div className="categories">
                  <h1> Categories </h1>
                  <div className="tinySquare1">
                    <h2 className="cat1 ">Bills</h2>
                    <p>$00.00</p>
                  </div>
                  <div className="tinySquare2">
                    <h2 className="cat2">Crypto</h2>
                    <p>$00.00</p>
                  </div>
                  <div className="tinySquare3">
                    <h2 className="cat3">Shopping</h2>
                    <p>$00.00</p>
                  </div>
                  <div className="tinySquare4">
                    <h2 className="cat4">Grocery</h2>
                    <p>$00.00</p>
                  </div>
                </div>
              </section>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
  }
const express = require("express");
const path = require("path");
const hbs = require("hbs");
const fs = require("fs");
const { Client } = require('pg');

const app = express();
const port = 8080;

app.set("view engine", "hbs");
app.set("views", path.join(__dirname, "../templates/views"));
app.use(express.static(path.join(__dirname, "../public")));
hbs.registerPartials(path.join(__dirname, "../templates/componets"));

const args = process.argv.slice(2);
let credentials = { user: "postgres", password: null };

for (let i = 0; i < args.length - 1; i++) {
    if (args[i] == "-u" || args[i] == "--user")
        credentials.user = args[i + 1];
    if (args[i] == "-p" || args[i] == "--password")
        credentials.password = args[i + 1];
}
const client = new Client({
    user: credentials.user,
    host: 'localhost',
    database: 'project',
    password: credentials.password,
    port: 5432,
});
client.connect();


app.get("/", (req, res) => {
    res.send(client.database + " " + client.user);
    //res.render("index", { title: "Index Page", name: "How", function: counter() });

});

app.get("/weather", (req, res) => {
    const location = req.query.location !== 'undefined' ? req.query.location : "Istanbul";
    console.log(location);
    weatherCalendar.weather(req.query.location, (response) => {
        res.render("info",
            {
                title: "Weather App",
                response: response
            }
        );
    });
});

app.get("/object", (req, res) => {
    if (!req.query.filter)
        res.send(JSON.stringify(req.query.filter));
    else
        res.send({ status: "error", error: "define a query" });
})
app.get("*", (req, res) => {
    res.render("info", { title: "404 Page Not Found", author: "Umut Sevdi", name: "It's no good", function: counter() });
})


jsonObject = fs.readFileSync("./package.json").toString();
app.get("/json", (req, res) => { res.send(jsonObject); })
app.listen(port, () => {
    console.log("Server is up on port " + port);
});
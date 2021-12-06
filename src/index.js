const express = require("express");
const path = require("path");
const hbs = require("hbs");
const fs = require("fs");


const app = express();
const port = 8080;

app.set("view engine", "hbs");

app.set("views", path.join(__dirname, "../templates/views"));
app.use(express.static(path.join(__dirname, "../public")));
hbs.registerPartials(path.join(__dirname, "../templates/componets"));


app.get("/", (req, res) => {
    res.render("index", { title: "Index Page", name: "How", function: counter() });

});

let num = 0;
function counter() {
    num += 1;
    return num;
}

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
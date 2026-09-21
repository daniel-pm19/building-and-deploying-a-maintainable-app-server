async function saludar() {
    const name = document.getElementById("name").value.trim();
    const language = document.getElementById("language").value;

    const respuesta = await fetch(
        "/hello?name=" + encodeURIComponent(name) +
        "&language=" + encodeURIComponent(language)
    );

    const texto = await respuesta.text();
    document.getElementById("result-hello").textContent = texto;
}

async function darPi() {
    const respuesta = await fetch("/pi");
    const texto = await respuesta.text();
    document.getElementById("result-pi").textContent = texto;
}

document.getElementById("btn-hello").addEventListener("click", saludar);
document.getElementById("btn-pi").addEventListener("click", darPi);

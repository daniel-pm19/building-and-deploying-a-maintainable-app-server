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

async function darE() {
    const respuesta = await fetch("/e");
    const texto = await respuesta.text();
    document.getElementById("result-e").textContent = texto;
}

async function darSin() {
    const n = document.getElementById("n").value.trim();

    const respuesta = await fetch("/sin?n=" + encodeURIComponent(n));

    const texto = await respuesta.text();
    document.getElementById("result-sin").textContent = texto;
}

async function buscarImagen() {
    const imageid = document.getElementById("imageid").value.trim();

    const respuesta = await fetch("/images?imageid=" + encodeURIComponent(imageid));

    const texto = await respuesta.text();
    document.getElementById("result-images").textContent = texto;
}

document.getElementById("btn-hello").addEventListener("click", saludar);
document.getElementById("btn-pi").addEventListener("click", darPi);
document.getElementById("btn-e").addEventListener("click", darE);
document.getElementById("btn-sin").addEventListener("click", darSin);
document.getElementById("btn-images").addEventListener("click", buscarImagen);

package eu.tutorials.licenta_yummy.util

/**
 * Transforma un cod HTTP intr-un mesaj pe care utilizatorul il poate intelege
 * si care spune exact ce trebuie facut.
 */
object ApiErrors {

    fun messageFor(code: Int, fallback: String? = null): String = when (code) {
        401 -> "Cheia API este invalida sau lipseste (401). Inlocuieste API_KEY in Constants.kt."
        402 -> "Cheia API si-a epuizat limita zilnica (402). Genereaza o cheie noua pe spoonacular.com si pune-o in Constants.kt."
        403 -> "Acces refuzat de server (403). Verifica daca abonamentul cheii mai este activ."
        404 -> "Adresa ceruta nu exista (404)."
        429 -> "Prea multe cereri intr-un timp scurt (429). Asteapta un minut si incearca din nou."
        in 500..599 -> "Serverul Spoonacular are o problema ($code). Incearca mai tarziu."
        else -> fallback ?: "Eroare de retea ($code)."
    }

    fun messageForException(e: Exception): String = when (e) {
        is java.net.UnknownHostException -> "Nu am putut contacta serverul. Verifica conexiunea la internet."
        is java.net.SocketTimeoutException -> "Serverul nu a raspuns la timp. Incearca din nou."
        else -> "Eroare la incarcarea retetelor: ${e.javaClass.simpleName}"
    }
}

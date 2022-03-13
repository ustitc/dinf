package dinf.routes

import dinf.html.templates.DiceForm
import dinf.html.templates.DiceFormWithInputs
import dinf.html.templates.Form

class ComponentDeps {

    fun diceForm(url: String): DiceForm {
        val form = Form(url)
        return DiceFormWithInputs(form)
    }

}

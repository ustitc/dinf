package dinf.routes

import dinf.domain.Dice
import dinf.html.pages.DiceEditPage
import dinf.html.templates.DiceForm
import dinf.html.templates.DiceFormWithLists
import dinf.html.templates.Form

class ComponentDeps {

    fun diceForm(url: String): DiceForm {
        val form = Form(url)
        return DiceFormWithLists(form)
    }

    fun diceEditPage(dice: Dice, editURL: String, deleteURL: String): DiceEditPage {
        return DiceEditPage(diceForm(editURL), dice, editURL, deleteURL)
    }

}

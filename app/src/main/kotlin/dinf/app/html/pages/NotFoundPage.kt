package dinf.app.html.pages

import dinf.app.html.templates.LayoutTemplate
import kotlinx.html.p

class NotFoundPage : Page {

    override fun LayoutTemplate.apply() {
        content {
            p {
                +"There is nothing here"
            }
        }
    }
}

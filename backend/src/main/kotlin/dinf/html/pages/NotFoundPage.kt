package dinf.html.pages

import dinf.html.templates.Layout
import kotlinx.html.p

class NotFoundPage : Page {

    override fun Layout.apply() {
        content {
            p {
                +"There is nothing here"
            }
        }
    }
}

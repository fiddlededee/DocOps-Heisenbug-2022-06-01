@Grab('org.ccil.cowan.tagsoup:tagsoup:1.2.1')

import org.ccil.cowan.tagsoup.Parser

def doc = new XmlSlurper(new Parser()).parse("screenshots/BillForm.html")
doc.depthFirst().findAll { it.name() == 'span' && it.@class == 'state-value'}
print doc.depthFirst().findAll { it.name() == 'span' && it.@class == 'state-value'}

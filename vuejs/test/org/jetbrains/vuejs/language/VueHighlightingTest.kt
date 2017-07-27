package org.jetbrains.vuejs.language

import com.intellij.codeInspection.htmlInspections.HtmlUnknownAttributeInspection
import com.intellij.codeInspection.htmlInspections.HtmlUnknownBooleanAttributeInspectionBase
import com.intellij.codeInspection.htmlInspections.HtmlUnknownTagInspection
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

/**
 * @author Irina.Chernushina on 7/19/2017.
 */
class VueHighlightingTest : LightPlatformCodeInsightFixtureTestCase() {
  override fun setUp() {
    super.setUp()
    myFixture.enableInspections(HtmlUnknownBooleanAttributeInspectionBase())
    myFixture.enableInspections(HtmlUnknownAttributeInspection())
    myFixture.enableInspections(HtmlUnknownTagInspection())
  }

  fun testDirectivesWithoutParameters() {
    myFixture.configureByText("directivesWithoutHighlighting.vue", "<template>\n" +
                                                                   "  <div v-once></div>\n" +
                                                                   "  <div v-else></div>\n" +
                                                                   "  <div v-pre></div>\n" +
                                                                   "  <div v-cloak></div>\n" +
                                                                   "</template>")
    myFixture.checkHighlighting()
  }

  fun testVIfRequireParameter() {
    myFixture.configureByText("vIfRequireParameter.vue",
                              "<template><div <warning descr=\"Wrong attribute value\">v-if</warning>></div></template>")
    myFixture.checkHighlighting()
  }

  fun testArrowFunctionsAndExpressionsInTemplate() {
    myFixture.configureByText("ArrowFunctionsAndExpressionsInTemplate.vue", """
<template>
<p>Completed Tasks: {{ ((todo) => todo.done === true)({done: 111}) }}</p>
<p>Pending Tasks: {{ todos.filter((todo) => {return todo.done === false}).length }}</p>
<div class="map" v-bind:class="{ 'map--loading': 'test', aaa: 118 }">Additional...</div>
{{todos}}
</template>
<script>
    let todos = 1;
</script>
""")
    myFixture.checkHighlighting()
  }

  fun testShorthandArrowFunctionInTemplate() {
    myFixture.configureByText("ShorthandArrowFunctionInTemplate.vue", """
<template>
    <div id="app">
        <div @event="val => bar = val"></div>
        {{bar}}
    </div>
</template>
<script>
    let bar = {};
</script>
""")
    myFixture.checkHighlighting()
  }

  fun testLocalPropsInArrayResolvedAndWithKebabCaseAlso() {
    myFixture.configureByText("LocalPropsInArrayResolvedAndWithKebabCaseAlso.vue",
                              """
<template>
    <div id="app">
        <camelCase one-two="test" <warning descr="Attribute three-four is not allowed here">three-four</warning>=1></camelCase>
        <camelCase oneTwo="test" <warning descr="Attribute three-four is not allowed here">three-four</warning>=1></camelCase>
    </div>
</template>
<script>
    export default {
      name: 'camelCase',
      props: ['oneTwo']
    }
</script>
""")
    myFixture.checkHighlighting()
  }

  fun testLocalPropsInObjectResolvedAndWithKebabCaseAlso() {
    myFixture.configureByText("LocalPropsInObjectResolvedAndWithKebabCaseAlso.vue",
                              """
<template>
    <div id="app">
        <camelCase one-two="test" <warning descr="Attribute three-four is not allowed here">three-four</warning>=1></camelCase>
        <camelCase oneTwo="test" <warning descr="Attribute three-four is not allowed here">three-four</warning>=1></camelCase>
    </div>
</template>
<script>
    export default {
      name: 'camelCase',
      props: {
        oneTwo: {}
      }
    }
</script>
""")
    myFixture.checkHighlighting()
  }

  fun testImportedComponentPropsAsArray() {
    myFixture.configureByText("compUI.vue", """
<script>
    export default {
        name: 'compUI',
        props: ['seeMe']
    }
</script>
""")
    myFixture.configureByText("ImportedComponentPropsAsArray.vue", """
<template>
    <div id="app">
        <comp-u-i see-me="12345" <warning descr="Attribute butNotThis is not allowed here">butNotThis</warning>="112"></comp-u-i>
        <comp-u-i seeMe="12345" <warning descr="Attribute butNotThis is not allowed here">butNotThis</warning>="112"></comp-u-i>
    </div>
</template>
<script>
    import CompUI from 'compUI.vue'
</script>
""")
    myFixture.checkHighlighting()
  }

  fun testImportedComponentPropsAsObject() {
    myFixture.configureByText("compUI.vue", """
<script>
    export default {
        name: 'compUI',
        props: {
          seeMe: {}
        }
    }
</script>
""")
    myFixture.configureByText("ImportedComponentPropsAsObject.vue", """
<template>
    <div id="app">
        <comp-u-i see-me="12345" <warning descr="Attribute butNotThis is not allowed here">butNotThis</warning>="112"></comp-u-i>
        <comp-u-i seeMe="12345" <warning descr="Attribute butNotThis is not allowed here">butNotThis</warning>="112"></comp-u-i>
    </div>
</template>
<script>
    import CompUI from 'compUI.vue'
</script>
""")
    myFixture.checkHighlighting()
  }
}
package acedochaidez.sergio.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View) {
        if (view is Button)
        {
            if (view.text == "."){
                if (canAddDecimal)
                    workingsTv.append(view.text)

                canAddDecimal = false
            }
            else
                workingsTv.append(view.text)

                canAddOperation = true
        }
        }


    fun operationAction(view: View) {
        if (view is Button && canAddOperation)
        {
            workingsTv.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }

    }
    fun allclearAction(view: View) {
        ResultsTv.text = ""
        workingsTv.text = ""

    }
    fun backSpaceAction(view: View) {
        val length = workingsTv.length()
        if (length > 0 )
            workingsTv.text = workingsTv.text.subSequence(0, length-1)
    }

    fun equalsAction(view: View) {
        ResultsTv.text = calculateResults()

    }

    private fun calculateResults(): String {
        val digitOperator = digitsOperators()
        if (digitOperator.isEmpty()) return ""

        val timesDivision = timedivisionCalculate(digitOperator)
        if (timesDivision.isEmpty()) return ""


        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result= passedList[0] as Float

        for (i in passedList.indices)
        {
        if (passedList[i] is Char && i != passedList.lastIndex)
        {
            val operator = passedList[i]
            val nextDigit = passedList[i +1 ] as Float
            if (operator == '+')
                result += nextDigit
            if (operator == '-')
                result -= nextDigit
        }
        }

        return  result
    }

    private fun timedivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
    var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
    list = calculateTimesDiv(list)
        }
        return list
    }

    private fun calculateTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
    val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i< restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nexDigit = passedList[i + 1] as Float
                when(operator)
                {
                'x' ->
                {
                    newList.add(prevDigit * nexDigit)
                    restartIndex = i + 1
                }'/' ->
                {
                    newList.add(prevDigit / nexDigit)
                    restartIndex = i + 1
                }
                    else->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
             }

            if ( i > restartIndex)
            newList.add(passedList[i])
        }

        return  newList
    }


    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in workingsTv.text)
        {
            if(character.isDigit() || character == '.')
            currentDigit += character
            else{
            list.add(currentDigit.toFloat())
            currentDigit = ""
            list.add(character)
            }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
}
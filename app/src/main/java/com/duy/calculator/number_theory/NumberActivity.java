/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.calculator.number_theory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.duy.calculator.R;
import com.duy.calculator.activities.abstract_class.AbstractEvaluatorActivity;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.item_math_type.NumberIntegerItem;

import java.util.ArrayList;

/**
 * Number activity
 * Created by Duy on 15-Feb-17.
 */

public class NumberActivity extends AbstractEvaluatorActivity {
    public static final String DATA = "DATA";
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            type = intent.getIntExtra(DATA, NumberType.CATALAN);
        }
        setup();
    }

    /**
     * set up interface
     * - label of btnSolve
     * - hint of mInputFormula
     */
    private void setup() {
        btnSolve.setText(R.string.eval);
        switch (type) {
            case NumberType.CATALAN:
                mHint1.setHint(getString(R.string.catalan_desc));
                setTitle(R.string.catalan_number);
                break;
            case NumberType.FIBONACCI:
                mHint1.setHint(getString(R.string.fibo_desc));
                setTitle(R.string.fibonacci);
                break;
            case NumberType.PRIME:
                mHint1.setHint(getString(R.string.prime_desc));
                setTitle(R.string.prime);
                break;
            case NumberType.GCD:
                setTitle(R.string.gcd);
                break;
            case NumberType.LCM:
                setTitle(R.string.lcm);
                break;
            default:
                mHint1.setHint(getString(R.string.enter_number));
                break;
        }
    }

    @Override
    public void clickEvaluate() {
        super.clickEvaluate();
        if (mInputFormula.getCleanText().isEmpty()) {
            mInputFormula.setError(getString(R.string.please_enter_number));
            return;
        }
        String input = mInputFormula.getCleanText();
        NumberIntegerItem item = new NumberIntegerItem(input);
        switch (type) {
            case NumberType.CATALAN:
                item.setCmd(NumberType.CATALAN_CMD);
                break;
            case NumberType.FIBONACCI:
                item.setCmd(NumberType.FIBONACCI_CMD);
                break;
            case NumberType.PRIME:
                item.setCmd(NumberType.PRIME_CMD);
                break;
            case NumberType.GCD:
                item.setCmd(NumberType.GCD_CMD);
                break;
            case NumberType.LCM:
                item.setCmd(NumberType.LCM_CMD);
                break;
            default:
                break;
        }
        //disable fraction mode
        MathEvaluator.getInstance().setFraction(true);
        //create new task eval and exec it with NumberIntegerItem
        new ATaskEval().execute(item);
    }

    @Override
    public int getIdStringHelp() {
        return 0;
    }

    @Override
    public void clickHelp() {

    }


    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {

                String primitiveStr = "Integrate(" + input + ",x)";
// TODO: 30-Jun-17  trig
                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(primitiveStr,
                        EvaluateConfig.loadFromSetting(getApplicationContext())
                                .setEvalMode(EvaluateConfig.FRACTION));

                String decimal = MathEvaluator.getInstance().evaluateWithResultAsTex(primitiveStr,
                        EvaluateConfig.loadFromSetting(getApplicationContext())
                                .setEvalMode(EvaluateConfig.DECIMAL));

                ArrayList<String> result = new ArrayList<>();
                result.add(fraction);
                result.add(decimal);
                return result;
            }
        };
    }
}

package com.example.triviatest.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.triviatest.controller.AppController;
import com.example.triviatest.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    private ArrayList<Question> questions = new ArrayList<>();

    private String url = "https://opentdb.com/api.php?amount=41&difficulty=easy&type=boolean";

    public ArrayList<Question> getQuestions(final AnswerListAsyncResponse callback) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                (JSONObject) null,

        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);

                        String question = obj.getString("question");
                        boolean answer = obj.getBoolean("correct_answer");

                        Question formedQ = new Question(question, answer);
                        questions.add(formedQ);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (callback != null) callback.processFinished(questions);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "onErrorResponse: " + error);
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


        return questions;
    }
}

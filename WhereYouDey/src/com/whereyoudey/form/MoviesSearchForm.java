/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.TextField;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.helper.Result;

/**
 *
 * @author Vikram S
 */
class MoviesSearchForm extends EventsSearchForm {

    MoviesSearchForm(WhereYouDey midlet) {
        super(midlet);
    }

    protected String getFormTitle() {
        return "Search Movie Show Times by City";
    }

    protected ResultForm getResultForm(Result[] results) {
        return new MoviesResultsForm(midlet, results, this);
    }

    protected int getSelectedIconPos() {
        return 3;
    }

    protected void searchAction() {
        results = searchService.searchMovies(city.getText().trim());
    }

}

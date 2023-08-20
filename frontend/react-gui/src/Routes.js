import React from 'react';
import { Route, Switch } from 'react-router-dom';

import HomePage from './pages/HomePage';
import ReactionsPage from './pages/ReactionsPage';
import OpinionChangesPage from './pages/OpinionChangesPage';


const Routes = () => {
  return (
    <>
      {/* A <Switch> looks through its children <Route>s and
          renders the first one that matches the current URL. */}
      <Switch>
        <Route path="/reactions">
          <ReactionsPage />
        </Route>
        <Route path="/opinion-changes">
          <OpinionChangesPage />
        </Route>
        <Route path="/">
          <HomePage />
        </Route>
      </Switch>
    </>
  );
};

export default Routes;

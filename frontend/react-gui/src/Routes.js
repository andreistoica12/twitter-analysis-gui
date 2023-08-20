import React from 'react';
import { Route, Switch } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Model2Page from './pages/Model2Page';
import Model3Page from './pages/Model3Page';

const Routes = () => {
  return (
    <>
      {/* A <Switch> looks through its children <Route>s and
          renders the first one that matches the current URL. */}
      <Switch>
        <Route path="/provenance/model2">
          <Model2Page />
        </Route>
        <Route path="/model3">
          <Model3Page />
        </Route>
        <Route path="/">
          <HomePage />
        </Route>
      </Switch>
    </>
  );
};

export default Routes;

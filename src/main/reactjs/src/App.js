import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { privateRoutes, publicRoutes } from "./routes/Routes";
import BlankLayout from "./components/layouts/BlankLayout";
import AuthenticatedRoute from "./components/AuthenticatedRoute";
import { ToastContainer } from "react-toastify";
import "./App.css";
function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <ToastContainer
          position="top-right"
          autoClose={5000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="colored"
        />
        <Routes>
          {publicRoutes.map((route, index) => {
            const Layout = route.layout || BlankLayout;
            const Page = route.component;
            return (
              <Route
                key={index}
                path={route.path}
                element={
                  <Layout>
                    <Page />
                  </Layout>
                }
              ></Route>
            );
          })}
          {privateRoutes.map((route, index) => {
            const Layout = route.layout || BlankLayout;
            const Page = route.component;
            return (
              <Route
                key={index}
                path={route.path}
                element={
                  <AuthenticatedRoute authorization={route.authorization}>
                    <Layout>
                      <Page />
                    </Layout>
                  </AuthenticatedRoute>
                }
              ></Route>
            );
          })}
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;

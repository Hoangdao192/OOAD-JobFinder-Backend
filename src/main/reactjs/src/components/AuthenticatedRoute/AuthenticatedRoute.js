import Authentication from "../../services/Authentication/Authentication";
import {Route, Navigate} from "react-router-dom";

/**
 * Represent route that need authenticated
 */
function AuthenticatedRoute({authorization, children}) {
    console.log(authorization)
    const isAuthenticated = Authentication.isUserAuthenticated();
    let havePermission = false;

    if (isAuthenticated) {
        const userRole = Authentication.getCurrentUser().role;
        if (authorization != undefined) {
            if (authorization.includes(userRole)) {
                havePermission = true;
            }
        } else {
            havePermission = true;
        }
    }
    return (
        isAuthenticated ? (
            havePermission ? children : <Navigate to="/not_found"/>
        ) : (
            <Navigate to="/login" />
        )
    )
}

export default AuthenticatedRoute;
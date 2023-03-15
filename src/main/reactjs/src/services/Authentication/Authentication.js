import config from '../../config.json';
import ServerMessageParser from '../ServerMessageParser';

/**
 * For authentication using JsonWebToken
 */
class Authentication {
    //  Check if current user is authenticated
    isUserAuthenticated() {
        let authToken = localStorage.getItem("authToken");
        if (authToken == null || authToken == undefined || authToken.length == 0) {
            return false;
        }
        return true;
    }

    generateAuthorizationHeader() {
        let authToken = localStorage.getItem("authToken");
        let tokenType = localStorage.getItem("tokenType");
        return tokenType + " " + authToken;
    }

    //  Get current user information
    getCurrentUser() {
        if (localStorage.getItem("user") != null) {
            return JSON.parse(localStorage.getItem("user"));
        }
    }

    //  Send login request to server
    //  Receive token and save to localStorage
    //  Get current user data from server and save to localStorage
    login(username, password) {
        return new Promise((resolve, reject) => {
            let request = new XMLHttpRequest();
            request.open("POST", config.server.api.login.url);
            request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            request.onload = function () {
                if (this.status >= 200 && this.status < 400) {
                    let response = JSON.parse(this.response);
                    let authToken = response["accessToken"];
                    let tokenType = response["tokenType"];
                    if (authToken == undefined || authToken == null || authToken.length == 0) {
                        reject("Không nhận được token đăng nhập");
                        // return false;
                    }
    
                    localStorage.setItem("authToken", authToken);
                    localStorage.setItem("tokenType", tokenType);

                    localStorage.setItem("user", response["user"]);
                    resolve()
                    // fetch(config.server.api.account.data.url, {
                    //     headers: {
                    //         'Authorization': tokenType + " " + authToken
                    //     }
                    // }).then((response) => {
                    //     if (response.status == 200) {
                    //         return response.json()
                    //     }
                    // }).then((data) => {
                    //     if (data != undefined) {
                    //         localStorage.setItem('user', JSON.stringify(data))
                    //         resolve()
                    //     }
                    // })
                    // return true;
                } else {
                    let errors = ServerMessageParser.parse(this.response)
                    reject(errors[0])
                }
            }
            request.send(JSON.stringify({
                email: username,
                password: password
            }))
        })
    }

    logout() {
        localStorage.removeItem("authToken");
        localStorage.removeItem("tokenType");
        localStorage.removeItem("user");
    }
}

export default new Authentication();
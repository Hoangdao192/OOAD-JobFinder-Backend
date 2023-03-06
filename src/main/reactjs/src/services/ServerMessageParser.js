class ServerMessageParser {
    parse(message) {
        let errors = JSON.parse(message);
        console.log(errors);
        let errorList = [];
        for (let val of Object.values(errors)) {
            errorList.push(val);
        }
        console.log(errorList);
        return errorList;
    }
}

export default new ServerMessageParser()
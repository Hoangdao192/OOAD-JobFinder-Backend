import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

function BlankLayout({children}) {
    return (
        <div>
            <div className="container">
                {children}
            </div>
        </div>
    )
}

export default BlankLayout;
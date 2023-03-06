import { Home } from "../pages/Home/Home"
import BlankLayout from "../components/layouts/BlankLayout/BlankLayout"

const privateRoutes = [
    //  Example: {path: '/manager/home', layout: ManagerLayout , component: ManagerHome, authorization : ['Admin']}
    //  Explain:
    //  path: path from root
    //  layout: layout for this page (BlankLayout as default)
    //  component: component will be display
    //  authorization: array of roles can access this page
]
const publicRoutes = [
    //  Example: {path: '/login', component: Login}
    {path: '/', component: Home, layout: BlankLayout}
]

export { publicRoutes, privateRoutes }
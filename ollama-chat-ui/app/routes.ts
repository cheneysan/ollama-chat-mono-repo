import {type RouteConfig, index, route} from '@react-router/dev/routes'

export default [
    index("routes/home.tsx"),
    route("/signin", "routes/signin.jsx"),
    route("/signup", "routes/signup.jsx"),
] satisfies RouteConfig;

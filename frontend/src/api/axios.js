import axios from "axios"

const api = axios.create({
  baseURL: "http://localhost:8080/api",
})

// 🔥 Request Interceptor (Attach JWT)
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token")

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  (error) => Promise.reject(error)
)

// 🔥 Response Interceptor (Handle 401)
api.interceptors.response.use(
  (response) => response,
  (error) => {

    const isAuthEndpoint = error.config?.url?.includes("/auth/login")

    if (
      error.response &&
      (error.response.status === 401 || error.response.status === 403) &&
      !isAuthEndpoint
    ) {
      localStorage.removeItem("token")
      window.location.href = "/"
    }

    return Promise.reject(error)
  }
)


export default api

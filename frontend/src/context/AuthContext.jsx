import { createContext, useContext, useState, useEffect } from "react"
import { jwtDecode } from "jwt-decode"

const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(
    localStorage.getItem("token")
  )

  const logout = () => {
    localStorage.removeItem("token")
    setToken(null)
  }

  const login = (newToken) => {
    localStorage.setItem("token", newToken)
    setToken(newToken)
  }

  // 🔥 Auto logout on token expiry
  useEffect(() => {
    if (!token) return

    try {
      const decoded = jwtDecode(token)

      if (!decoded.exp) return

      const expirationTime = decoded.exp * 1000
      const currentTime = Date.now()
      const timeout = expirationTime - currentTime

      if (timeout <= 0) {
        logout()
      } else {
        const timer = setTimeout(() => {
          logout()
        }, timeout)

        return () => clearTimeout(timer)
      }

    } catch (error) {
      logout()
    }
  }, [token])

  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)

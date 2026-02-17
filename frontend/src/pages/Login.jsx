import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { loginUser } from "../api/authApi"
import { useAuth } from "../context/AuthContext"
import { Link } from "react-router-dom"
import toast from "react-hot-toast"

function Login() {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const navigate = useNavigate()
  const { login } = useAuth()

  const [errors, setErrors] = useState({})
const [loading, setLoading] = useState(false)

const handleLogin = async (e) => {
  e.preventDefault()

  setLoading(true)
  setErrors({})

  try {
    const data = await loginUser(email, password)

    const token = data.data.token
    login(token)

    toast.success("Login successful")
    navigate("/dashboard")

  } catch (error) {
    if (error.response?.data?.data) {
      setErrors(error.response.data.data)
    } else if (error.response?.data?.message) {
      setErrors({ general: error.response.data.message })
    } else {
      setErrors({ general: "Login failed" })
    }
    toast.error(error.response?.data?.message || "Login failed")

  } finally {
    setLoading(false)
  }
}


  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form
        onSubmit={handleLogin}
        className="bg-white p-8 rounded-lg shadow-md w-96"
      >
        <h2 className="text-2xl font-bold mb-6 text-center">
          Login
        </h2>
        {/* {errors.general && (
          <p className="text-red-500 text-sm mb-4">
            {errors.general}
          </p>
        )} */}

        <input
          type="email"
          placeholder="Email"
          className="w-full p-3 border rounded mb-4"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Password"
          className="w-full p-3 border rounded mb-6"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white p-3 rounded hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? "Logging in..." : "Login"}
        </button>

        <p className="text-center mt-4 text-sm">
          Don’t have an account?{" "}
          <Link
            to="/register"
            className="text-blue-600 hover:underline"
          >
            Register
          </Link>
        </p>

      </form>
    </div>
  )
}

export default Login

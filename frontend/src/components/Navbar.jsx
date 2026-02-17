import { Link, useNavigate } from "react-router-dom"
import { useAuth } from "../context/AuthContext"

function Navbar() {
  const navigate = useNavigate()
  const { logout } = useAuth()

  const handleLogout = () => {
  const confirmLogout = window.confirm(
    "Are you sure you want to logout?"
  )

  if (confirmLogout) {
    logout()
    navigate("/")
  }
}


  return (
    <div className="bg-white shadow">
      <div className="max-w-6xl mx-auto px-6 py-4 flex justify-between items-center">
        <h1 className="text-xl font-bold text-blue-600">
          Smart Finance Manager
        </h1>

        <div className="space-x-6">
          <Link
            to="/dashboard"
            className="text-gray-700 hover:text-blue-600"
          >
            Dashboard
          </Link>

          <Link
            to="/transactions"
            className="text-gray-700 hover:text-blue-600"
          >
            Transactions
          </Link>

          <button
            onClick={handleLogout}
            className="text-red-600 hover:text-red-800 transition duration-200 cursor-pointer"
          >
            Logout
          </button>
        </div>
      </div>
    </div>
  )
}

export default Navbar

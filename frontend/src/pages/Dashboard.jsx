import { useEffect, useState } from "react"
import { getDashboardSummary } from "../api/dashboardApi"
import Navbar from "../components/Navbar"
import { getMonthlySummary } from "../api/dashboardApi"
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ResponsiveContainer,
  Legend,
} from "recharts"


function Dashboard() {
  const [summary, setSummary] = useState(null)
  const [monthlyData, setMonthlyData] = useState([])

    useEffect(() => {
    const fetchData = async () => {
        try {
        const summaryRes = await getDashboardSummary()
        setSummary(summaryRes.data)

        const monthlyRes = await getMonthlySummary()

        // Format data for chart
        const formatted = monthlyRes.data.map((item) => ({
            month: `${item.year}-${String(item.month).padStart(2, "0")}`,
            income: item.income,
            expense: item.expense,
        }))

        setMonthlyData(formatted)

        } catch (error) {
        console.error(error)
        }
    }

    fetchData()
    }, [])


  return (
    <>
    <Navbar />
    <div className="min-h-screen bg-gray-100 p-10">
      <h1 className="text-3xl font-bold mb-8">Dashboard</h1>

      {summary && (
        <div className="grid grid-cols-3 gap-6">
          <div className="bg-white p-6 rounded shadow">
            <h2 className="text-lg font-semibold">Total Income</h2>
            <p className="text-2xl text-green-600">
              ₹ {summary.totalIncome}
            </p>
          </div>

          <div className="bg-white p-6 rounded shadow">
            <h2 className="text-lg font-semibold">Total Expense</h2>
            <p className="text-2xl text-red-600">
              ₹ {summary.totalExpense}
            </p>
          </div>

          <div className="bg-white p-6 rounded shadow">
            <h2 className="text-lg font-semibold">Net Savings</h2>
            <p className="text-2xl text-blue-600">
              ₹ {summary.netSavings}
            </p>
          </div>
        </div>
      )}
      <div className="bg-white p-6 rounded shadow mt-10">
        <h2 className="text-xl font-semibold mb-6">
            Monthly Income vs Expense
        </h2>

        <ResponsiveContainer width="100%" height={300}>
            <BarChart data={monthlyData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="month" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="income" fill="#16a34a" />
            <Bar dataKey="expense" fill="#dc2626" />
            </BarChart>
        </ResponsiveContainer>
        </div>
    </div>
    </>
  )
}

export default Dashboard

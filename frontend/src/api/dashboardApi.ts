import api from "./axios"

export const getDashboardSummary = async () => {
  const response = await api.get("/dashboard/summary")
  return response.data
}

export const getMonthlySummary = async () => {
  const response = await api.get("/dashboard/monthly")
  return response.data
}

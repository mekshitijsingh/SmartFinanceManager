import api from "./axios"

export const getTransactions = async (
  page = 0,
  size = 5,
  sortBy = "date",
  direction = "desc"
) => {
  const response = await api.get("/transactions", {
    params: {
      page,
      size,
      sortBy,
      direction,
    },
  })

  return response.data
}

export const createTransaction = async (transaction) => {
  const response = await api.post("/transactions", transaction)
  return response.data
}

export const deleteTransaction = async (id) => {
  const response = await api.delete(`/transactions/${id}`)
  return response.data
}

export const updateTransaction = async (id, transaction) => {
  const response = await api.put(`/transactions/${id}`, transaction)
  return response.data
}

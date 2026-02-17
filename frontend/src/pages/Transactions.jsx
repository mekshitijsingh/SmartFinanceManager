import { useEffect, useState } from "react"
import { getTransactions } from "../api/transactionApi"
import { createTransaction } from "../api/transactionApi"
import Navbar from "../components/Navbar"
import { deleteTransaction } from "../api/transactionApi"
import { updateTransaction } from "../api/transactionApi"
import toast from "react-hot-toast"

function Transactions() {
  const [data, setData] = useState(null)
  const [page, setPage] = useState(0)
  const [form, setForm] = useState({
    amount: "",
    type: "INCOME",
    category: "",
    description: "",
    date: "",
  })
  const [editingId, setEditingId] = useState(null)
  const [editForm, setEditForm] = useState({})


  const handleChange = (e) => {
    setForm({
        ...form,
        [e.target.name]: e.target.value,
    })
  }

const handleSubmit = async (e) => {
  e.preventDefault()

  try {
    await createTransaction(form)

    setForm({
      amount: "",
      type: "INCOME",
      category: "",
      description: "",
      date: "",
    })

    const response = await getTransactions(page)
    setData(response.data)
    toast.success("Transaction added successfully")
    } catch (error) {
        console.error(error)
        toast.error("Failed to add transaction")
    }
  }

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm(
        "Are you sure you want to delete this transaction?"
    )

    if (!confirmDelete) return

    try {
        await deleteTransaction(id)

        const response = await getTransactions(page)
        setData(response.data)
        toast.success("Transaction deleted successfully")

    } catch (error) {
        console.error(error)
        toast.error("Failed to delete transaction")
    }
  }

  const startEdit = (tx) => {
  setEditingId(tx.id)
  setEditForm({
    amount: tx.amount,
    type: tx.type,
    category: tx.category,
    description: tx.description,
    date: tx.date,
  })
}

const handleEditChange = (e) => {
  setEditForm({
    ...editForm,
    [e.target.name]: e.target.value,
  })
}

const handleUpdate = async (id) => {
  try {
    await updateTransaction(id, editForm)
    setEditingId(null)

    const response = await getTransactions(page)
    setData(response.data)
    toast.success("Transaction updated successfully")
  } catch (error) {
    console.error(error)
    toast.error("Failed to update transaction")
  }
}

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        const response = await getTransactions(page)
        setData(response.data)
      } catch (error) {
        console.error(error)
      }
    }

    fetchTransactions()
  }, [page])

  if (!data) return <div className="p-10">Loading...</div>

return (
  <>
  <Navbar />
  <div className="min-h-screen bg-gray-100 p-10">
    <div className="max-w-6xl mx-auto">
      <h1 className="text-3xl font-bold mb-8">
        Transactions
      </h1>
      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded-lg shadow mb-6 grid grid-cols-5 gap-4"
        >
        <input
            type="number"
            name="amount"
            placeholder="Amount"
            value={form.amount}
            onChange={handleChange}
            className="p-2 border rounded"
            required
        />

        <select
            name="type"
            value={form.type}
            onChange={handleChange}
            className="p-2 border rounded"
        >
            <option value="INCOME">INCOME</option>
            <option value="EXPENSE">EXPENSE</option>
        </select>

        <input
            type="text"
            name="category"
            placeholder="Category"
            value={form.category}
            onChange={handleChange}
            className="p-2 border rounded"
            required
        />

        <input
            type="date"
            name="date"
            value={form.date}
            onChange={handleChange}
            className="p-2 border rounded"
            required
        />

        <button
            type="submit"
            className="bg-blue-600 text-white rounded"
        >
            Add
        </button>

        <input
            type="text"
            name="description"
            placeholder="Description"
            value={form.description}
            onChange={handleChange}
            className="col-span-5 p-2 border rounded"
        />
        </form>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-gray-200">
            <tr>
              <th className="p-4">Date</th>
              <th className="p-4">Type</th>
              <th className="p-4">Amount</th>
              <th className="p-4">Category</th>
              <th className="p-4">Description</th>
              <th className="p-4">Actions</th>
            </tr>
          </thead>
          <tbody>
            {data.content.map((tx) => (
              <tr key={tx.id} className="border-t hover:bg-gray-50">

                {editingId === tx.id ? (
                  <>
                    <td className="p-4">
                      <input
                        type="date"
                        name="date"
                        value={editForm.date}
                        onChange={handleEditChange}
                        className="border p-1 rounded"
                      />
                    </td>

                    <td className="p-4">
                      <select
                        name="type"
                        value={editForm.type}
                        onChange={handleEditChange}
                        className="border p-1 rounded"
                      >
                        <option value="INCOME">INCOME</option>
                        <option value="EXPENSE">EXPENSE</option>
                      </select>
                    </td>

                    <td className="p-4">
                      <input
                        type="number"
                        name="amount"
                        value={editForm.amount}
                        onChange={handleEditChange}
                        className="border p-1 rounded"
                      />
                    </td>

                    <td className="p-4">
                      <input
                        type="text"
                        name="category"
                        value={editForm.category}
                        onChange={handleEditChange}
                        className="border p-1 rounded"
                      />
                    </td>

                    <td className="p-4">
                      <input
                        type="text"
                        name="description"
                        value={editForm.description}
                        onChange={handleEditChange}
                        className="border p-1 rounded"
                      />
                    </td>

                    <td className="p-4 space-x-2">
                      <button
                        onClick={() => handleUpdate(tx.id)}
                        className="text-green-600 cursor-pointer"
                      >
                        Save
                      </button>

                      <button
                        onClick={() => setEditingId(null)}
                        className="text-gray-600 cursor-pointer"
                      >
                        Cancel
                      </button>
                    </td>
                  </>
                ) : (
                  <>
                    <td className="p-4">{tx.date}</td>
                    <td className="p-4">{tx.type}</td>
                    <td className="p-4 font-medium">₹ {tx.amount}</td>
                    <td className="p-4">{tx.category}</td>
                    <td className="p-4">{tx.description}</td>

                    <td className="p-4 space-x-3">
                      <button
                        onClick={() => startEdit(tx)}
                        className="text-blue-600 cursor-pointer"
                      >
                        Edit
                      </button>

                      <button
                        onClick={() => handleDelete(tx.id)}
                        className="text-red-600 cursor-pointer"
                      >
                        Delete
                      </button>
                    </td>
                  </>
                )}

              </tr>
            ))}

          </tbody>
        </table>
      </div>

      {/* Pagination */}
      <div className="flex items-center justify-between mt-6">
        <button
          disabled={page === 0}
          onClick={() => setPage(page - 1)}
          className="bg-gray-300 px-4 py-2 rounded disabled:opacity-50"
        >
          Previous
        </button>

        <span className="font-medium">
          Page {data.currentPage + 1} of {data.totalPages}
        </span>

        <button
          disabled={page >= data.totalPages - 1}
          onClick={() => setPage(page + 1)}
          className="bg-gray-300 px-4 py-2 rounded disabled:opacity-50"
        >
          Next
        </button>
      </div>
    </div>
  </div>
  </>
)

}

export default Transactions

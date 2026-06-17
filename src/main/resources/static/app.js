const form = document.querySelector("#transactionForm");
const refreshButton = document.querySelector("#refreshButton");
const testNotFoundButton = document.querySelector("#testNotFoundButton");
const transactionsBody = document.querySelector("#transactionsBody");
const transactionCount = document.querySelector("#transactionCount");
const apiOutput = document.querySelector("#apiOutput");
const totalIncome = document.querySelector("#totalIncome");
const totalExpense = document.querySelector("#totalExpense");
const balance = document.querySelector("#balance");
const transactionDate = document.querySelector("#transactionDate");

transactionDate.value = new Date().toISOString().slice(0, 10);

function formatCurrency(value) {
    return new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
    }).format(Number(value || 0));
}

function showOutput(title, data) {
    apiOutput.textContent = `${title}\n\n${typeof data === "string" ? data : JSON.stringify(data, null, 2)}`;
}

function markStep(step) {
    const item = document.querySelector(`[data-step="${step}"]`);
    if (item) {
        item.classList.add("done");
    }
}

async function requestJson(url, options = {}) {
    const response = await fetch(url, options);
    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
        throw data || { status: response.status, message: response.statusText };
    }

    return data;
}

async function loadHealth() {
    const response = await fetch("/health");
    const text = await response.text();
    markStep("health");
    showOutput("GET /health", text);
}

async function loadTransactions() {
    const transactions = await requestJson("/transactions");
    markStep("list");
    renderTransactions(transactions);
    showOutput("GET /transactions", transactions);
}

async function loadSummary() {
    const summary = await requestJson("/summary");
    markStep("summary");
    totalIncome.textContent = formatCurrency(summary.totalIncome);
    totalExpense.textContent = formatCurrency(summary.totalExpense);
    balance.textContent = formatCurrency(summary.balance);
}

async function refreshAll() {
    await loadTransactions();
    await loadSummary();
}

function renderTransactions(transactions) {
    transactionCount.textContent = `${transactions.length} ${transactions.length === 1 ? "record" : "records"}`;

    if (transactions.length === 0) {
        transactionsBody.innerHTML = '<tr><td colspan="7" class="empty">No transactions yet.</td></tr>';
        return;
    }

    transactionsBody.innerHTML = transactions.map((transaction) => {
        const typeClass = transaction.type === "INCOME" ? "income" : "expense";
        return `
            <tr>
                <td>${transaction.id}</td>
                <td>${transaction.transactionDate || ""}</td>
                <td>${transaction.type}</td>
                <td>${transaction.category || ""}</td>
                <td>${transaction.description || ""}</td>
                <td class="amount ${typeClass}">${formatCurrency(transaction.amount)}</td>
                <td>
                    <button class="delete-button" type="button" data-id="${transaction.id}">Delete</button>
                </td>
            </tr>
        `;
    }).join("");
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const formData = new FormData(form);
    const payload = {
        amount: Number(formData.get("amount")),
        type: formData.get("type"),
        category: formData.get("category"),
        description: formData.get("description"),
        transactionDate: formData.get("transactionDate"),
    };

    try {
        const created = await requestJson("/transactions", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        markStep("create");
        showOutput("POST /transactions", created);
        form.reset();
        transactionDate.value = new Date().toISOString().slice(0, 10);
        await refreshAll();
    } catch (error) {
        markStep("error");
        showOutput("Request failed", error);
    }
});

transactionsBody.addEventListener("click", async (event) => {
    const button = event.target.closest("[data-id]");
    if (!button) {
        return;
    }

    const id = button.dataset.id;

    try {
        await requestJson(`/transactions/${id}`, { method: "DELETE" });
        showOutput(`DELETE /transactions/${id}`, "Deleted.");
        await refreshAll();
    } catch (error) {
        markStep("error");
        showOutput("Delete failed", error);
    }
});

refreshButton.addEventListener("click", async () => {
    try {
        await refreshAll();
    } catch (error) {
        markStep("error");
        showOutput("Refresh failed", error);
    }
});

testNotFoundButton.addEventListener("click", async () => {
    try {
        await requestJson("/transactions/999999");
    } catch (error) {
        markStep("error");
        showOutput("GET /transactions/999999", error);
    }
});

loadHealth()
    .then(refreshAll)
    .catch((error) => {
        markStep("error");
        showOutput("Startup check failed", error);
    });

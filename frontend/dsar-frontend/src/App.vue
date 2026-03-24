<template>
  <div class="container">
    <h1>DSAR Portal</h1>

    <div class="section">
      <h2>Login Details</h2>
      <input v-model="username" placeholder="Username" />
      <input v-model="password" type="password" placeholder="Password" />
    </div>

    <div class="section">
      <h2>Submit DSAR Request</h2>
      <input v-model="customerName" placeholder="Customer Name" />
      <input v-model="email" placeholder="Email" />
      <select v-model="requestType">
        <option>ACCESS</option>
        <option>DELETE</option>
        <option>CORRECT</option>
      </select>
      <textarea v-model="description" placeholder="Description"></textarea>
      <button @click="submitRequest">Submit Request</button>
    </div>

    <div class="section">
      <h2>All Requests</h2>
      <button @click="loadRequests">Load Requests</button>
      <table v-if="requests.length">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Type</th>
            <th>Status</th>
            <th>New Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="req in requests" :key="req.id">
            <td>{{ req.id }}</td>
            <td>{{ req.customerName }}</td>
            <td>{{ req.email }}</td>
            <td>{{ req.requestType }}</td>
            <td>{{ req.status }}</td>
            <td>
              <select v-model="req.newStatus">
                <option>SUBMITTED</option>
                <option>IN_PROGRESS</option>
                <option>COMPLETED</option>
                <option>REJECTED</option>
              </select>
            </td>
            <td>
              <button @click="updateStatus(req.id, req.newStatus)">Update</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="section">
      <h2>Audit Logs</h2>
      <button @click="loadAuditLogs">Load Audit Logs</button>
      <table v-if="auditLogs.length">
        <thead>
          <tr>
            <th>ID</th>
            <th>Action</th>
            <th>User</th>
            <th>Timestamp</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="log in auditLogs" :key="log.id">
            <td>{{ log.id }}</td>
            <td>{{ log.action }}</td>
            <td>{{ log.performedBy }}</td>
            <td>{{ log.timestamp }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <p class="message">{{ message }}</p>
  </div>
</template>

<script>
import api from "./services/api";

export default {
  data() {
    return {
      username: "admin",
      password: "password",
      customerName: "",
      email: "",
      requestType: "ACCESS",
      description: "",
      requests: [],
      auditLogs: [],
      message: ""
    };
  },
  methods: {
    getAuthConfig() {
      return {
        auth: {
          username: this.username,
          password: this.password
        }
      };
    },
    async submitRequest() {
      try {
        await api.post(
          "/requests",
          {
            customerName: this.customerName,
            email: this.email,
            requestType: this.requestType,
            description: this.description
          },
          this.getAuthConfig()
        );
        this.message = "Request submitted successfully";
        this.customerName = "";
        this.email = "";
        this.requestType = "ACCESS";
        this.description = "";
      } catch (error) {
        this.message = "Failed to submit request";
      }
    },
    async loadRequests() {
      try {
        const response = await api.get("/requests", this.getAuthConfig());
        this.requests = response.data.map((r) => ({
          ...r,
          newStatus: r.status
        }));
        this.message = "Requests loaded successfully";
      } catch (error) {
        this.message = "Failed to load requests";
      }
    },
    async updateStatus(id, status) {
      try {
        await api.put(`/requests/${id}?status=${status}`, {}, this.getAuthConfig());
        this.message = "Request status updated successfully";
        this.loadRequests();
      } catch (error) {
        this.message = "Failed to update request status";
      }
    },
    async loadAuditLogs() {
      try {
        const response = await api.get("/audit", this.getAuthConfig());
        this.auditLogs = response.data;
        this.message = "Audit logs loaded successfully";
      } catch (error) {
        this.message =
          "Failed to load audit logs: " +
          (error.response?.status || error.message);
      }
    }
  }
};
</script>

<style>
* {
  box-sizing: border-box;
}

body {
  margin: 0;
  font-family: Arial, sans-serif;
  background: linear-gradient(135deg, #eef2ff, #f8fafc);
  color: #1f2937;
}

#app {
  width: 100%;
}

.container {
  max-width: 1100px;
  margin: 30px auto;
  padding: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #1e3a8a;
}

h2 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #111827;
  font-size: 20px;
}

.section {
  background: #ffffff;
  padding: 24px;
  margin-bottom: 24px;
  border-radius: 14px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

input,
select,
textarea {
  display: block;
  width: 100%;
  max-width: 520px;
  margin: 10px 0;
  padding: 12px 14px;
  font-size: 14px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  background: #f9fafb;
}

textarea {
  min-height: 90px;
  resize: vertical;
}

button {
  margin-top: 10px;
  padding: 10px 18px;
  font-size: 14px;
  font-weight: 600;
  color: white;
  background: #2563eb;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

button:hover {
  background: #1d4ed8;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 16px;
  background: white;
  border-radius: 10px;
  overflow: hidden;
}

th {
  background: #eff6ff;
  color: #1e3a8a;
  font-weight: 700;
}

th,
td {
  border: 1px solid #e5e7eb;
  padding: 12px;
  text-align: left;
  font-size: 14px;
}

tr:nth-child(even) {
  background: #f9fafb;
}

.message {
  margin-top: 10px;
  font-weight: 600;
  color: #059669;
}

@media (max-width: 768px) {
  .container {
    padding: 14px;
  }

  .section {
    padding: 16px;
  }

  th,
  td {
    font-size: 12px;
    padding: 8px;
  }
}
</style>
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
      username: "customer",
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
        this.message = "Failed to load audit logs";
      }
    }
  }
};
</script>

<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  background: #f4f6f8;
}

.container {
  max-width: 1100px;
  margin: 20px auto;
  padding: 20px;
}

.section {
  background: white;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
}

input,
select,
textarea,
button {
  display: block;
  width: 100%;
  max-width: 500px;
  margin: 10px 0;
  padding: 10px;
  font-size: 14px;
}

button {
  width: auto;
  cursor: pointer;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
  background: white;
}

table,
th,
td {
  border: 1px solid #ccc;
}

th,
td {
  padding: 10px;
  text-align: left;
}

.message {
  font-weight: bold;
  color: green;
}
</style>
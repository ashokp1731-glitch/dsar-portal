<h1>DSAR Portal</h1> 
<h2>Submit Request</h2> 
<input v-model="customerName" placeholder="Customer Name" />
<br /><br /> 
<input v-model="email" placeholder="Email" />
<br /><br /> 
<select v-model="requestType"> 
  <option>ACCESS</option> 
  <option>DELETE</option> 
  <option>CORRECT</option> 
</select> 
<br /><br />

<button @click="submitRequest">Submit Request</button> 
<hr /> 
<h2>All Requests (Admin)</h2> 
<button @click="loadRequests">Load Requests</button> 
<ul> 
  <li v-for="r in requests" :key="r.id"> 
    {{ r.customerName }} - {{ r.requestType }} - {{ r.status }} 
  </li> 
</ul>

<template id="tips-overview">
  <app-layout>
    <div class="tips-container ">
      <h3>Health Tips</h3>
      <ul>
        <li v-for="tip in healthtips" :key="tip.id">
          <a class="tip-link">{{tip.tips}}</a>
        </li>
      </ul>
      <div class="col" align="centre">
        <button rel="tooltip" title="Refresh"
                class="btn btn-info btn-simple btn-link"
                @click="fetchRandomTip">
          <i class="fa fa-refresh" aria-hidden="true"></i>
        </button>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component("tips-overview", {
  template: "#tips-overview",
  data: () => ({
    healthtips: [],
  }),
  created() {
    this.fetchRandomTip();
  },
  methods: {
    fetchHealthTips: function () {
      axios.get("/api/tip")
          .then(res => {
            console.log("Response from server:", res);
            this.healthtips = res.data;
          })
          .catch(error => {
            console.error("Error while fetching healthtips:", error);
            alert("Error while fetching healthtips");
          });
    },
    fetchRandomTip: function () {
      axios.get("/api/tip")
          .then(res => {
            console.log("Response from server:", res);
            this.healthtips = res.data;
          })
          .catch(error => {
            console.error("Error while fetching healthtips:", error);
            alert("Error while fetching healthtips");
          });
    }
  }
});
</script>
</script>

<style scoped>
.tips-container {
  text-align: center;
}
.tip-link {
  color: #000000;
  text-decoration: none;
  font-weight: bold;
  margin-right: 10px;
  margin-top: 7px;
  font-size: 25px; /* Set your desired font size */
  font-family: 'Lora', serif;
 }

ul {
  list-style-type: none;
  padding: 0;
}

button {
  background-color: #87CEEB;
  color: #fff;
  padding: 10px;
  cursor: pointer;
  border: none;
  border-radius: 4px;
}

 .tips-container {
   background-color: #87CEEB; /* Sky Blue */
   opacity: 0.7;
 }



</style>




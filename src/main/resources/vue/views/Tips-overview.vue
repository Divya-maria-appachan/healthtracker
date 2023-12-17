<template id="Tips-overview">
  <app-layout>
    <h3>Healthtips list</h3>
    <ul>
      <li v-for="tip in healthtips" :key="tip.id">

        <a :href="`/tip/${tip.id}`">{{ tip.tips }}</a>
      </li>
    </ul>
  </app-layout>
</template>

<script>
app.component("Tips-overview", {
  template: "#Tips-overview",
  data: () => ({
    healthtips: [],
  }),
  created() {
    // Corrected method name
    this.fetchHealthTips();
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
    }
  }
});
</script>

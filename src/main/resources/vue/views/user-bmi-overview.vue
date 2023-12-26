<template id="user-bmi-overview">
  <app-layout>
    <div>
      <h3>Bmi list </h3>
      <ul>


      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-bmi-overview",{
  template: "#user-bmi-overview",
  data: () => ({
    bmi: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/bmi`)
        .then(res => this.bmi = res.data)
        .catch(() => alert("Error while fetching bmi"));
  }
});
</script>
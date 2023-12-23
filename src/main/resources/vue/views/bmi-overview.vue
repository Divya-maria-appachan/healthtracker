<template>
  <div>
    <h2>BMI Calculator</h2>
    <form @submit.prevent="calculateBmi">
      <label for="weight">Weight (kg): </label>
      <input type="number" v-model="weight" required />

      <label for="height">Height (cm): </label>
      <input type="number" v-model="height" required />

      <button type="submit">Calculate BMI</button>
    </form>

    <div v-if="bmiResult">
      <h3>Your BMI Result:</h3>
      <p>{{ bmiResult }}</p>
    </div>
  </div>
</template>

<script>
app.component("user-bmi",{
  template: "#user-bmi",
  data: () => ({
      weight: null,
      height: null,
      bmiResult: null,
    }),
  created() {

  },

  methods: {
    calculateBmi() {
      // Implement BMI calculation logic here
      // You can use an API call to your backend or perform the calculation in the frontend
      // Update this.bmiResult with the calculated BMI
    },
  },
};



</script>

<style scoped>
/* Add your component styles here */
</style>
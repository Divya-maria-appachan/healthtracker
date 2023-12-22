<template id="user-sleep">
  <app-layout>
    <h4>Sleep Tracker</h4>
    <br>
    <div class="chart-container"
         style="position: relative; height:40vh; width:85vw; border: 1px solid lightgrey; padding: 25px">
      <canvas id="myChart"></canvas>
    </div>
    <br>
    <p v-if="sleep.length === 0">No Data Found! Add your sleep duration now to track health status</p>
    <br>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Timeline </div>


          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn shadow-none border-0" data-toggle="modal" data-target="#myModal">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>


      <!-- Modal -->
      <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modalTitle">Today</h5>
              <button type="button" class="close" data-dismiss="modal">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <form ref="myForm">
                <div class="form-group">
                  <label for="inpSleep">Sleep Duration(Hour)</label>
                  <input type="text" class="form-control" id="inpSleep" v-model="formData.duration" placeholder="Enter your sleephr...">
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              <button type="button" data-dismiss="modal" class="btn btn-primary" @click="addSleep();">Save changes</button>
            </div>
          </div>
        </div>
      </div>


      <!-- Modal -->
      <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="modalTitle">{{new Date(currentDate).toLocaleDateString('en-us', { weekday: "long", year:"numeric", month:"short", day:"numeric"})}}</h5>
              <button type="button" class="close" data-dismiss="modal">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <form>
                <div class="form-group">
                  <label for="inpSleep">Enter new Sleep Duration</label>
                  <input type="text" class="form-control" id="inpSleep" v-model="formData.duration">
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              <button type="button" data-dismiss="modal" class="btn btn-primary" @click="updateSleep(currentId, currentDate);">Save changes</button>
            </div>
          </div>
        </div>
      </div>


      <div class="card-body" v-for="(w, index) in sleep" v-bind:key="index">
        <table class="table">
          <tbody>
          <tr>
            <th scope="row">{{ w.duration }} Hrs</th>
            <td>{{ new Date(w.date).toLocaleDateString('en-us', { weekday: "long", year:"numeric", month:"short", day:"numeric"}) }}</td>
            <td class="float-right">
              <button rel="tooltip" title="Update" @click=getData(w) data-toggle="modal" data-target="#updateModal"
                      class="btn btn-info btn-simple btn-link"
              >
                <i class="far fa-save" aria-hidden="true"></i>
              </button>
              <button rel="tooltip" title="Delete"
                      class="btn btn-info btn-simple btn-link"
                      @click="deleteSleep(w, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>


  </app-layout>
</template>

<script>
app.component("user-sleep",{
  template: "#user-sleep",
  data: () => ({
    sleep: [],
    dates: [],
    duration: [],
    formData: [],
    currentId: null,
    currentDate: null
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/sleep`)
        .then(res => {
          this.sleep = res.data
          if(this.sleep.length) {
            const ctx = document.getElementById('myChart');
            for (item of this.sleep) {
              this.dates.push(new Date(item.date).toLocaleDateString('en-us', { year:"numeric", month:"short", day:"numeric"}) )
              this.duration.push(item.duration)
            }
            this.dates.sort(function(a, b) {
              return new Date(a) - new Date(b);
            });
            new Chart(ctx, {
              type: 'line',
              data: {
                labels: this.dates,
                datasets: [{
                  label: '# of Votes',
                  data: this.duration,
                  borderWidth: 1
                }]
              },
              options: {
                scales: {
                  y: {
                    beginAtZero: false
                  },
                  x: {
                    grid: {
                      display: false,
                    }
                  },
                  y: {
                    grid: {
                      display: false
                    }
                  },
                },
                plugins: {
                  legend: {
                    display: false
                  }
                }
              }
            })
          }

        })
        .catch(() => console.log("Error while fetching sleep Tracker"));

  },
  methods: {
    getData: function (w) {

      this.currentId=w.id;
      this.currentDate=w.date;

    },
    addSleep: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}/sleep`
      axios.post(url,
          {
            duration: parseFloat(this.formData.duration),
            date: new Date().toISOString(),
            userId: userId
          })
          .then(response =>
              this.sleep.push(response.data))
          .catch(error => {
            console.log(error)
          })

    },
    updateSleep: function (id, date) {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/sleep/${id}`
      axios.patch(url,
          {
            duration: parseFloat(this.formData.duration),
            date: date,
            userId: userId
          })
          .then(response =>
              this.sleep.push(response.data))
          .catch(error => {
            console.log(error)
          })
    },
    deleteSleep: function (sleep, index) {
      if (confirm('Are you sure you want to delete?')) {
        const url = `/api/sleep/${sleep.id}`;
        axios.delete(url)
            .then(response =>
                this.sleep.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
  },



});



</script>
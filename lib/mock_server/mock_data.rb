require 'faker'
require 'date'

class MockDataGenerator
  private
  def get_mock
    {
      :name => Faker::Name.name,
      :email => Faker::Internet.email,
      :ccn => Faker::Business.credit_card_number,
      :profile_image => Faker::Avatar.image(Faker::Lorem.words(4).join('-'), "50x50"),
      :age => Faker::Number.number(2),
      :bio => Faker::Hipster.paragraphs(1),
      :created_at => Faker::Date.between(Date.civil(Date.today.year - 1), Date.today)
    }
  end

  public
  def get_mocks(n)
    (1..n).map { |n| get_mock }
  end
end
